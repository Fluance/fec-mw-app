package net.fluance.cockpit.core.repository.jpa.refdata;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.fluance.cockpit.core.model.jpa.refdata.RefdataEntity;
import net.fluance.cockpit.core.model.jpa.refdata.RefdataPK;

public interface RefdataRepository extends JpaRepository<RefdataEntity, RefdataPK> {

    @Cacheable(value = "refdataByCompanyCodeCache")
    @Query(value = "SELECT r FROM RefdataEntity r WHERE r.id.src = :src AND r.id.srctable = :srctable AND r.id.company = :companyCode AND r.id.code = :code AND r.lang = :language")
    RefdataEntity findByIdCompanyAndIdCodeAndLang(@Param("src") String src, @Param("srctable") String srctable, @Param("companyCode") String companyCode, @Param("code") String code, @Param("language") String language);

    @Cacheable(value = "refdataByCompanyCodesCache")
    @Query(value = "SELECT r FROM RefdataEntity r WHERE r.id.src = :src AND r.id.srctable = :srctable AND r.id.company = :companyCode AND r.id.code IN :codes AND r.lang = :language")
    List<RefdataEntity> findByCompanyCodeAndCodesAndLang(@Param("src") String src, @Param("srctable") String srctable, @Param("companyCode") String companyCode, @Param("codes") List<String> codes, @Param("language") String language);

}