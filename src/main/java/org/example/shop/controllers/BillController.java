package org.example.shop.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.shop.entities.Bill;
import org.example.shop.repo.BillRepo;
import org.example.shop.service.BillService;
import org.example.shop.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    BillService billService;
    @Autowired
    BillRepo billRepo;

    @PostMapping("/create")
    public ResponseEntity<Response<Boolean>> create(@RequestBody Bill bill){
        try {
            boolean bills = billService.create(bill);
            return  ResponseEntity.status(HttpStatus.OK).body(new Response<Boolean>(bills,"OKs"));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<Boolean>(false,e.getMessage()));

        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<Response<List<Bill>>> getAll(@RequestParam("page") int page , @RequestParam("limit") int limit){
        try {
            List<Bill> bills = billService.getAll(page, limit);
            long count = billRepo.count();
            return  ResponseEntity.status(HttpStatus.OK).body(new Response<List<Bill>>(count,bills,"OKs"));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<List<Bill>>(null,e.getMessage()));

        }
    }

    @GetMapping("/user")
    public ResponseEntity< Response<List<Bill>>> getBillByUser(@RequestParam("userId") String userId) {
        try {
            List<Bill> bills = billService.getBillByUser(userId);
            return  ResponseEntity.status(HttpStatus.OK).body(new Response<List<Bill>>(0,bills,"Ok"));
        }catch (Exception e ){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<List<Bill>>(null,e.getMessage()));
        }
    }

    @GetMapping("/getById/")
    public ResponseEntity< Response<Bill>> getBillByUser(@RequestParam("id") long id) {
        try {
            Optional<Bill> bill=  billRepo.findById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(new Response<Bill>(bill.get(),"Ok"));
        }catch (Exception e ){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<Bill>(null,e.getMessage()));
        }
    }


    @PostMapping("/pdf")
    public ResponseEntity<byte[]> exportBillsAsPdf(@RequestBody List<Long> billIds) {
        List<Bill> bills = billService.getBillsByIds(billIds);
        byte[] pdfBytes = generatePdf(bills);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    private byte[] generatePdf(List<Bill> bills) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            float sum = 0;
            document.open();
            for (Bill bill : bills) {
                document.add(new Paragraph("Mã hóa đơn: " + bill.getId()));
                document.add(new Paragraph("Khách hàng: " + bill.getAccount().getFirstName()+ bill.getAccount().getLastName()));
                document.add(new Paragraph("Sản phẩm: " +bill.getProduct().getTitle()+"      "+"Giá:" + bill.getProduct().getPrice() +"vnd          " +"Số lượng: x" + bill.getQuantity()));
                document.add(new Paragraph("-------------------------------------------"));
                sum+= bill.getQuantity()*bill.getProduct().getPrice();
            }
            document.add(new Paragraph("Tổng cộng  hàng: " +sum));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }



}
