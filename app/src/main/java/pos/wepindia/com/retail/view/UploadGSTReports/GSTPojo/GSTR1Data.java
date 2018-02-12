package pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo;

import java.util.ArrayList;

import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1_B2CL.GSTR1_B2CL_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1_CDN.GSTR1_CDN_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1_DocDetails.GSTR1_DOCS_DetailData;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1_HSN.GSTR1_HSN_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR_Amend.GSTR1_B2B_A_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1_B2B.GSTR1_B2B_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR_Amend.GSTR1_B2CL_A_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR_Amend.GSTR1_B2CS_A_Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1_B2CS.GSTR1_B2CS_Data;

/**
 * Created by PriyabratP on 21-11-2016.
 */

public class GSTR1Data {

    private String gstin;
    private String fp;
    private double gt;
    private ArrayList<GSTR1_B2B_Data> b2b;
    private ArrayList<GSTR1_B2B_A_Data> b2ba;
    private ArrayList<GSTR1_B2CL_Data> b2cl;
    private ArrayList<GSTR1_B2CL_A_Data> b2cla;
    private ArrayList<GSTR1_B2CS_Data> b2cs;
    private ArrayList<GSTR1_B2CS_A_Data> b2csa;
    private ArrayList<GSTR1_CDN_Data> cdnr;
    private ArrayList<GSTR1_HSN_Data> hsn;
    private ArrayList<GSTR1_DOCS_DetailData> doc_issue;

    public GSTR1Data() {
    }
    public GSTR1Data(String gstin, String fp, double gt, ArrayList<GSTR1_B2CS_Data> b2cs, ArrayList<GSTR1_B2CS_A_Data> b2csa, ArrayList<GSTR1_CDN_Data> cdn) {
        this.gstin = gstin;
        this.fp = fp;
        this.gt = gt;
        this.b2cs = b2cs;
        this.b2csa = b2csa;
        this.cdnr = cdn;
    }

    public GSTR1Data(String gstin, String fp, ArrayList<GSTR1_B2B_Data> b2b, ArrayList<GSTR1_B2B_A_Data> b2ba, ArrayList<GSTR1_B2CL_Data> b2cl, ArrayList<GSTR1_B2CL_A_Data> b2cla, ArrayList<GSTR1_B2CS_Data> b2cs, ArrayList<GSTR1_B2CS_A_Data> b2csa, ArrayList<GSTR1_CDN_Data> cdnr, ArrayList<GSTR1_HSN_Data> hsn, ArrayList<GSTR1_DOCS_DetailData> doc_issue) {
        this.gstin = gstin;
        this.fp =fp;
        this.b2b = b2b;
        this.b2ba = b2ba;
        this.b2cl = b2cl;
        this.b2cla = b2cla;
        this.b2cs = b2cs;
        this.b2csa = b2csa;
        this.cdnr = cdnr;
        this.hsn = hsn;
        this.doc_issue = doc_issue;
    }

    public GSTR1Data(String gstin, String fp, double gt, ArrayList<GSTR1_B2B_Data> b2b, ArrayList<GSTR1_B2B_A_Data> b2ba, ArrayList<GSTR1_B2CL_Data> b2cl, ArrayList<GSTR1_B2CL_A_Data> b2cla, ArrayList<GSTR1_B2CS_Data> b2cs, ArrayList<GSTR1_B2CS_A_Data> b2csa, ArrayList<GSTR1_CDN_Data> cdn, ArrayList<GSTR1_HSN_Data> hsn) {
        this.gstin = gstin;
        this.fp = fp;
        this.gt = gt;
        this.b2b = b2b;
        this.b2ba = b2ba;
        this.b2cl = b2cl;
        this.b2cla = b2cla;
        this.b2cs = b2cs;
        this.b2csa = b2csa;
        this.cdnr = cdn;
        this.hsn = hsn;
    }

    public ArrayList<GSTR1_B2CL_Data> getB2cl() {
        return b2cl;
    }

    public void setB2cl(ArrayList<GSTR1_B2CL_Data> b2cl) {
        this.b2cl = b2cl;
    }

    public ArrayList<GSTR1_B2CL_A_Data> getB2cla() {
        return b2cla;
    }

    public void setB2cla(ArrayList<GSTR1_B2CL_A_Data> b2cla) {
        this.b2cla = b2cla;
    }

    public ArrayList<GSTR1_HSN_Data> getHsn() {
        return hsn;
    }

    public void setHsn(ArrayList<GSTR1_HSN_Data> hsn) {
        this.hsn = hsn;
    }

    public ArrayList<GSTR1_B2B_A_Data> getB2ba() {
        return b2ba;
    }

    public void setB2ba(ArrayList<GSTR1_B2B_A_Data> b2ba) {
        this.b2ba = b2ba;
    }

    public ArrayList<GSTR1_B2B_Data> getB2b() {
        return b2b;
    }

    public void setB2b(ArrayList<GSTR1_B2B_Data> b2b) {
        this.b2b = b2b;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public double getGt() {
        return gt;
    }

    public void setGt(double gt) {
        this.gt = gt;
    }

    public ArrayList<GSTR1_B2CS_Data> getB2cs() {
        return b2cs;
    }

    public void setB2cs(ArrayList<GSTR1_B2CS_Data> b2cs) {
        this.b2cs = b2cs;
    }

    public ArrayList<GSTR1_B2CS_A_Data> getB2csa() {
        return b2csa;
    }

    public void setB2csa(ArrayList<GSTR1_B2CS_A_Data> b2csa) {
        this.b2csa = b2csa;
    }

    public ArrayList<GSTR1_CDN_Data> getCdnr() {
        return cdnr;
    }

    public void setCdnr(ArrayList<GSTR1_CDN_Data> cdnr) {
        this.cdnr = cdnr;
    }
}
