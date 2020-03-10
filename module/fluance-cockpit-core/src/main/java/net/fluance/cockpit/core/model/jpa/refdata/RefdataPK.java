package net.fluance.cockpit.core.model.jpa.refdata;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class RefdataPK implements Serializable {

    private static final long serialVersionUID = 6645488137707106513L;

    private String src;
    private String srctable;
    private String company;
    private String code;

    public String getSrc() {  return src; }
    public String getSrctable() { return srctable; }
    public String getCompany() { return company; }
    public String getCode() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefdataPK refdataPK = (RefdataPK) o;
        return src.equals(refdataPK.src) &&
                srctable.equals(refdataPK.srctable) &&
                company.equals(refdataPK.company) &&
                code.equals(refdataPK.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, srctable, company, code);
    }

}
