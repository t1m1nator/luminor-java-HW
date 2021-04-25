package com.example.luminorjava;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
public class WebController {

    @Autowired
    private PaymentRepository paymentRepository;
    private LocationService locationService;


    @PostMapping("/payments")
    public String addPayment(@RequestParam BigDecimal amount, @RequestParam String debtorIban, HttpServletRequest request) throws IOException, GeoIp2Exception {

        if (validate(amount, debtorIban)) {
            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setDebtorIban(debtorIban);
            payment.setCreatedAt(new Date());

            //attempt to resolve country by IP
            LocationService locationService = new LocationService();
            try{
            payment.setCountry(locationService.getCountryLocation(request.getRemoteAddr()));
            //payment.setCountry(locationService.getCountryLocation("31.177.15.255"));

            } catch (Exception e){}
            paymentRepository.save(payment);
            return "Added new payment to repo!";
        }
        else
        return "Error, incorrect payment information!";
    }

    @PostMapping("/payments-files")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        // parse CSV file to create a list of `Payment` objects
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            //set locationservice for IP resolution later
            LocationService locationService = new LocationService();
            // create csv bean reader
            CsvToBean<Payment> csvToBean = new CsvToBeanBuilder(reader).withType(Payment.class).withIgnoreLeadingWhiteSpace(true).build();

            // convert `CsvToBean` object to list of payments
            // if csv files are expected to be large; should rewrite this section to reduce memory load
            List<Payment> payments = csvToBean.parse();
            for (Payment payment : payments){
                if(validate(payment.getAmount(), payment.getDebtorIban())){
                    try{
                        payment.setCountry(locationService.getCountryLocation(request.getRemoteAddr()));
                    } catch (Exception e){}
                    paymentRepository.save(payment);}
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error in uploading CSV file";
        }

        return "CSV file uploaded";
    }

    //Has optional debtorIban filter which will only return payments with matching IBAN
    @GetMapping("/payments")
    public Object getPayments(@RequestParam(required = false) String debtorIban) {
        if(debtorIban != null){
            return paymentRepository.findPaymentsByDebtorIban(debtorIban);
        }
        return paymentRepository.findAll();
    }


    //Method for validating Payment amount(greater than 0) and debtorIban(beginning with LT, LV or EE)
    public static boolean validate(BigDecimal amount, String debtorIban) {
        if (amount.compareTo(BigDecimal.ZERO) >= 0 && (debtorIban.substring(0, 2).equals("LT") || debtorIban.substring(0, 2).equals("LV") || debtorIban.substring(0, 2).equals("EE"))) {
            return true;
        }
        else return false;
    }
    //CSV reader for creating Payments from CSV file
    public void CsvReader(String file) {
        try {

            FileReader filereader = new FileReader(file);

            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
                for (int i = 1; i< nextRecord.length; i++){
                    if(validate(new BigDecimal(nextRecord[i]), nextRecord[i+1])) {
                        Payment payment = new Payment(new BigDecimal(nextRecord[i]), nextRecord[i + 1]);
                        paymentRepository.save(payment);

                    }
                    i++;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
