package ieetu.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchVO extends ApiDefaultVO {
    /**
     * 페이지갯수
     */
    private int pageUnit = 10;

    /**
     * 페이지사이즈
     */
    private int pageSize = 10;

    /**
     * 현재페이지
     */
    private int pageIndex = 1;

    /**
     * 첫페이지 인덱스
     */
    private int firstIndex = 1;

    /**
     * 마지막페이지 인덱스
     */
    private int lastIndex = 1;

    /**
     * 페이지당 레코드 개수
     */
    private int recordCountPerPage = 10;

    /**
     * 검색종류1
     */
    private String searchCnd1 = "";

    /**
     * 검색종류2
     */
    private String searchCnd2 = "";

    /**
     * 검색종류3
     */
    private String searchCnd3 = "";

    /**
     * 검색종류4
     */
    private String searchCnd4 = "";

    /**
     * 검색종류5
     */
    private String searchCnd5 = "";

    /**
     * 검색종류5
     */
    private String searchCnd6 = "";

    /**
     * 검색종류5
     */
    private String searchCnd7 = "";

    /**
     * 검색어
     */
    private String searchWrd = "";

    private String searchWrd2 = "";

    /**
     * 검색일자 시작일
     */
    private String searchStartDate = "";

    /**
     * 검색일자 종료일
     */
    private String searchEndDate = "";

    /**
     * 검색일자
     */
    private String searchDate = "";

    private String order = "1";

}
