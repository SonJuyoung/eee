package ieetu.common.paging;

import ieetu.common.utils.CommonFnc;
import ieetu.common.vo.ApiPagingVO;
import ieetu.common.vo.SearchVO;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.servlet.http.HttpServletRequest;

public class PagingHelper {
    public static void setDefaultLimitAndOffset(ApiPagingVO apiPagingVO, HttpServletRequest request) {
        String limit = CommonFnc.emptyCheckString("limit", request);
        String offset = CommonFnc.emptyCheckString("offset", request);
        if ("".equals(limit)) {
            limit = "10";
        }
        if ("".equals(offset)) {
            offset = "0";
        }

        apiPagingVO.setLimit(Integer.parseInt(limit));
        apiPagingVO.setOffset(Integer.parseInt(offset));
    }

    public static PaginationInfo getDefaultPaginationInfo(SearchVO comDefaultVO) {
        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(comDefaultVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(comDefaultVO.getRecordCountPerPage());
        paginationInfo.setPageSize(comDefaultVO.getPageSize());

        comDefaultVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        comDefaultVO.setLastIndex(paginationInfo.getLastRecordIndex());
        comDefaultVO.setPageSize(paginationInfo.getPageSize());

        return paginationInfo;
    }

    public static BasePaginationInfo getPaginationInfo(SearchVO comDefaultVO, String path) {
        BasePaginationInfo paginationInfo = new BasePaginationInfo();

        paginationInfo.setCurrentPageNo(comDefaultVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(comDefaultVO.getRecordCountPerPage());
        paginationInfo.setPageSize(comDefaultVO.getPageSize());

        comDefaultVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        comDefaultVO.setLastIndex(paginationInfo.getLastRecordIndex());
        comDefaultVO.setPageSize(paginationInfo.getPageSize());

        StringBuilder url = new StringBuilder();
        url.append("'").append(path).append("'");

        paginationInfo.setUrl(url.toString());
        return paginationInfo;
    }
}
