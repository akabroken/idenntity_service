/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.provider.kra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;

/**
 *
 * @author ndegel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimsModel {

    private TimsModel REQUEST;
    private TimsModel BATCHHEADER;
    private TimsModel BATCHDETAILS;
    
    private ArrayList<TimsModel> INVOICE;
    private ArrayList<TimsModel> ItemDetails;

    private String HASH;

    private String DateOfTransmission;
    private String DateOfTransaction;
    private String NumberOfLastInvoiceSent;
    private String PINOfSupplier;
    private String MiddlewareSerialNumber;
    private String NumberOfInvoiceRecords;
    private String TraderSystemInvoiceNumber;
    private String MiddlewareInvoiceNumber;

    private String RelevantInvoiceNumber;
    private String InvoiceDate;
    private String PINOfBuyer;
    private String Discount;
    private String InvoiceType;
    private String InvoiceCategory;
    private String TotalInvoiceAmount;
    private String TotalTaxableAmount;

    private String TotalTaxAmount;
    private String ExemptionNumber;
    private String QRCode;
    private String HSCode;
    private String HSDesc;
    private String Quantity;
    private String UnitPrice;
    private String ItemAmount;
    private String TaxRate;
    private String TaxAmount;

    public TimsModel getREQUEST() {
        return REQUEST;
    }

    public void setREQUEST(TimsModel REQUEST) {
        this.REQUEST = REQUEST;
    }

    public TimsModel getBATCHHEADER() {
        return BATCHHEADER;
    }

    public void setBATCHHEADER(TimsModel BATCHHEADER) {
        this.BATCHHEADER = BATCHHEADER;
    }

    public TimsModel getBATCHDETAILS() {
        return BATCHDETAILS;
    }

    public void setBATCHDETAILS(TimsModel BATCHDETAILS) {
        this.BATCHDETAILS = BATCHDETAILS;
    }

    public ArrayList<TimsModel> getINVOICE() {
        return INVOICE;
    }

    public void setINVOICE(ArrayList<TimsModel> INVOICE) {
        this.INVOICE = INVOICE;
    }

    public ArrayList<TimsModel> getItemDetails() {
        return ItemDetails;
    }

    public void setItemDetails(ArrayList<TimsModel> ItemDetails) {
        this.ItemDetails = ItemDetails;
    }

    public String getHASH() {
        return HASH;
    }

    public void setHASH(String HASH) {
        this.HASH = HASH;
    }

    public String getDateOfTransmission() {
        return DateOfTransmission;
    }

    public void setDateOfTransmission(String DateOfTransmission) {
        this.DateOfTransmission = DateOfTransmission;
    }

    public String getDateOfTransaction() {
        return DateOfTransaction;
    }

    public void setDateOfTransaction(String DateOfTransaction) {
        this.DateOfTransaction = DateOfTransaction;
    }

    public String getNumberOfLastInvoiceSent() {
        return NumberOfLastInvoiceSent;
    }

    public void setNumberOfLastInvoiceSent(String NumberOfLastInvoiceSent) {
        this.NumberOfLastInvoiceSent = NumberOfLastInvoiceSent;
    }

    public String getPINOfSupplier() {
        return PINOfSupplier;
    }

    public void setPINOfSupplier(String PINOfSupplier) {
        this.PINOfSupplier = PINOfSupplier;
    }

    public String getMiddlewareSerialNumber() {
        return MiddlewareSerialNumber;
    }

    public void setMiddlewareSerialNumber(String MiddlewareSerialNumber) {
        this.MiddlewareSerialNumber = MiddlewareSerialNumber;
    }

    public String getNumberOfInvoiceRecords() {
        return NumberOfInvoiceRecords;
    }

    public void setNumberOfInvoiceRecords(String NumberOfInvoiceRecords) {
        this.NumberOfInvoiceRecords = NumberOfInvoiceRecords;
    }

    public String getTraderSystemInvoiceNumber() {
        return TraderSystemInvoiceNumber;
    }

    public void setTraderSystemInvoiceNumber(String TraderSystemInvoiceNumber) {
        this.TraderSystemInvoiceNumber = TraderSystemInvoiceNumber;
    }

    public String getMiddlewareInvoiceNumber() {
        return MiddlewareInvoiceNumber;
    }

    public void setMiddlewareInvoiceNumber(String MiddlewareInvoiceNumber) {
        this.MiddlewareInvoiceNumber = MiddlewareInvoiceNumber;
    }

    public String getRelevantInvoiceNumber() {
        return RelevantInvoiceNumber;
    }

    public void setRelevantInvoiceNumber(String RelevantInvoiceNumber) {
        this.RelevantInvoiceNumber = RelevantInvoiceNumber;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String InvoiceDate) {
        this.InvoiceDate = InvoiceDate;
    }

    public String getPINOfBuyer() {
        return PINOfBuyer;
    }

    public void setPINOfBuyer(String PINOfBuyer) {
        this.PINOfBuyer = PINOfBuyer;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public String getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(String InvoiceType) {
        this.InvoiceType = InvoiceType;
    }

    public String getInvoiceCategory() {
        return InvoiceCategory;
    }

    public void setInvoiceCategory(String InvoiceCategory) {
        this.InvoiceCategory = InvoiceCategory;
    }

    public String getTotalInvoiceAmount() {
        return TotalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(String TotalInvoiceAmount) {
        this.TotalInvoiceAmount = TotalInvoiceAmount;
    }

    public String getTotalTaxableAmount() {
        return TotalTaxableAmount;
    }

    public void setTotalTaxableAmount(String TotalTaxableAmount) {
        this.TotalTaxableAmount = TotalTaxableAmount;
    }

    public String getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(String TotalTaxAmount) {
        this.TotalTaxAmount = TotalTaxAmount;
    }

    public String getExemptionNumber() {
        return ExemptionNumber;
    }

    public void setExemptionNumber(String ExemptionNumber) {
        this.ExemptionNumber = ExemptionNumber;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getHSCode() {
        return HSCode;
    }

    public void setHSCode(String HSCode) {
        this.HSCode = HSCode;
    }

    public String getHSDesc() {
        return HSDesc;
    }

    public void setHSDesc(String HSDesc) {
        this.HSDesc = HSDesc;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public String getItemAmount() {
        return ItemAmount;
    }

    public void setItemAmount(String ItemAmount) {
        this.ItemAmount = ItemAmount;
    }

    public String getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(String TaxRate) {
        this.TaxRate = TaxRate;
    }

    public String getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(String TaxAmount) {
        this.TaxAmount = TaxAmount;
    }
    
}
