package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderArchiveMapper {

    long countNeedArchivePo(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    long countArchivedPo(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    long countMissingPo(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    long countDifferentPo(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    List<String> fetchNextPoBatchIds(@Param("archiveBeforeDate") LocalDate archiveBeforeDate,
                                     @Param("batchSize") int batchSize,
                                     @Param("deleteSourceEnabled") boolean deleteSourceEnabled);

    int updateDifferentPoByIds(@Param("poIds") List<String> poIds);

    int insertMissingPoByIds(@Param("poIds") List<String> poIds);

    long countMissingPoByIds(@Param("poIds") List<String> poIds);

    long countDifferentPoByIds(@Param("poIds") List<String> poIds);

    int deleteSourcePoByIds(@Param("poIds") List<String> poIds);

    long countSourcePoRemainingByIds(@Param("poIds") List<String> poIds);

    long countNeedArchivePoc(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    long countArchivedPoc(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    long countMissingPoc(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    long countDifferentPoc(@Param("archiveBeforeDate") LocalDate archiveBeforeDate);

    int updateDifferentPocByPoIds(@Param("poIds") List<String> poIds);

    int insertMissingPocByPoIds(@Param("poIds") List<String> poIds);

    long countMissingPocByPoIds(@Param("poIds") List<String> poIds);

    long countDifferentPocByPoIds(@Param("poIds") List<String> poIds);

    int deleteSourcePocByPoIds(@Param("poIds") List<String> poIds);

    long countSourcePocRemainingByPoIds(@Param("poIds") List<String> poIds);
}
