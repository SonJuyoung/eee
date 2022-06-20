package ieetu.common.paging;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.text.MessageFormat;

public class BasePaginationRenderer extends AbstractPaginationRenderer implements ServletContextAware {
    private ServletContext servletContext;

    public BasePaginationRenderer() {

    }

    /* css 구현에 따라 커스텀 */
    public void initVariables() {
        firstPageLabel = "<a href=\"#\" title=\"맨처음 페이지\" onclick=\"{0}({1},{2});return false; \"><img src=\"../../images/page_prev_ago.png\"></a>";
        previousPageLabel = "<a href=\"#\" title=\"이전페이지\"onclick=\"{0}({1},{2});return false; \"><img src=\"../../images/page_prev.png\"></a>";
        currentPageLabel = "<a href=\"#\" title=\"선택된 페이지\" class=\"on\">{0}</a>";
        otherPageLabel = "<a href=\"#\" onclick=\"{0}({1},{3});return false; \">{2}</a>";
        nextPageLabel = "<a href=\"#\" title=\"다음페이지\" onclick=\"{0}({1},{2});return false; \"><img src=\"../../images/page_next.png\"></a>";
        lastPageLabel = "<a href=\"#\" title=\"맨끝 페이지\" onclick=\"{0}({1},{2});return false; \"><img src=\"../../images/page_next_ago.png\"></a>";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        initVariables();
    }

    public String renderPagination(PaginationInfo paginationInfo, String jsFunction) {

        StringBuffer strBuff = new StringBuffer();

        int firstPageNo = paginationInfo.getFirstPageNo();
        int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
        int totalPageCount = paginationInfo.getTotalPageCount();
        int pageSize = paginationInfo.getPageSize();
        int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
        int currentPageNo = paginationInfo.getCurrentPageNo();
        int lastPageNo = paginationInfo.getLastPageNo();

        /*
         * jsFunction 으로 url을 전달하기 위해 BasePaginationInfo로 형변환
         * 함수의 두번째 인자로 url 전달
         */
        String url = ((BasePaginationInfo) paginationInfo).getUrl();

//        if (totalPageCount > pageSize) {
        if (firstPageNoOnPageList > pageSize) {
            strBuff.append(MessageFormat.format(firstPageLabel, new Object[]{jsFunction, Integer.toString(firstPageNo), url}));
            strBuff.append(MessageFormat.format(previousPageLabel, new Object[]{jsFunction, Integer.toString(firstPageNoOnPageList - 1), url}));
        } else {
            strBuff.append(MessageFormat.format(firstPageLabel, new Object[]{jsFunction, Integer.toString(firstPageNo), url}));
            strBuff.append(MessageFormat.format(previousPageLabel, new Object[]{jsFunction, Integer.toString(firstPageNo), url}));
        }
//        }

        for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
            if (i == currentPageNo) {
                strBuff.append(MessageFormat.format(currentPageLabel, new Object[]{Integer.toString(i)}));
            } else {
                strBuff.append(MessageFormat.format(otherPageLabel, new Object[]{jsFunction, Integer.toString(i), Integer.toString(i), url}));
            }
        }

//        if (totalPageCount > pageSize) {
        if (lastPageNoOnPageList < totalPageCount) {
            strBuff.append(MessageFormat.format(nextPageLabel, new Object[]{jsFunction, Integer.toString(firstPageNoOnPageList + pageSize), url}));
            strBuff.append(MessageFormat.format(lastPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo), url}));
        } else {
            strBuff.append(MessageFormat.format(nextPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo), url}));
            strBuff.append(MessageFormat.format(lastPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo), url}));
        }
//        }
        return strBuff.toString();
    }
}
