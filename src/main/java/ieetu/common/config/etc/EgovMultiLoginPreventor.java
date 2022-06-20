package ieetu.common.config.etc;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class EgovMultiLoginPreventor {
    public static ConcurrentHashMap<String, HttpSession> loginUsers = new ConcurrentHashMap<String, HttpSession>();

    public static boolean findByLoginId(String loginId) {
        return loginUsers.containsKey(loginId);
    }

    public static void invalidateByLoginId(String loginId) {
        Enumeration<String> e = loginUsers.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            if (key.equals(loginId)) {
                try {
                    loginUsers.get(key).invalidate();
                } catch (Exception e1) {

                }
            }
        }
    }

    public static HttpSession findSessionBySessionId(String sessionId) {
        HttpSession session = null;
        Enumeration<String> e = loginUsers.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            HttpSession loginSession = loginUsers.get(key);
            if (sessionId.equals(loginSession.getId())) {
                session = loginSession;
                break;
            }
        }

        return session;
    }
}