package com.claude.mcp.service;

import com.claude.mcp.model.Cookie;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CookieManagerTest {
    private static final String TEST_DOMAIN = "test.com";
    private static final String COOKIE_STORE_FILE = "cookie_store.json";
    private CookieManager cookieManager;
    private Path cookieStorePath;

    @BeforeEach
    void setUp() {
        cookieManager = new CookieManager();
        cookieStorePath = Paths.get(COOKIE_STORE_FILE);
    }

    @AfterEach
    void tearDown() throws Exception {
        cookieManager.shutdown();
        Files.deleteIfExists(cookieStorePath);
    }

    @Test
    @Order(1)
    void testAddAndGetCookie() {
        Cookie cookie = new Cookie("test_cookie", "test_value");
        cookieManager.addCookie(TEST_DOMAIN, cookie);

        Set<Cookie> cookies = cookieManager.getCookies(TEST_DOMAIN);
        assertEquals(1, cookies.size());
        Cookie retrievedCookie = cookies.iterator().next();
        assertEquals("test_cookie", retrievedCookie.getName());
        assertEquals("test_value", retrievedCookie.getValue());
    }

    @Test
    @Order(2)
    void testGetExpiredCookie() {
        Cookie expiredCookie = new Cookie("expired_cookie", "expired_value");
        expiredCookie.setExpires(Instant.now().minusSeconds(3600)); // 1 hora en el pasado
        cookieManager.addCookie(TEST_DOMAIN, expiredCookie);

        Set<Cookie> cookies = cookieManager.getCookies(TEST_DOMAIN);
        assertTrue(cookies.isEmpty(), "No deberían retornarse cookies expiradas");
    }

    @Test
    @Order(3)
    void testRemoveCookie() {
        Cookie cookie = new Cookie("remove_test", "test_value");
        cookieManager.addCookie(TEST_DOMAIN, cookie);
        
        cookieManager.removeCookie(TEST_DOMAIN, "remove_test");
        Set<Cookie> cookies = cookieManager.getCookies(TEST_DOMAIN);
        assertTrue(cookies.isEmpty());
    }

    @Test
    @Order(4)
    void testClearCookies() {
        Cookie cookie1 = new Cookie("cookie1", "value1");
        Cookie cookie2 = new Cookie("cookie2", "value2");
        cookieManager.addCookie(TEST_DOMAIN, cookie1);
        cookieManager.addCookie(TEST_DOMAIN, cookie2);

        cookieManager.clearCookies(TEST_DOMAIN);
        Set<Cookie> cookies = cookieManager.getCookies(TEST_DOMAIN);
        assertTrue(cookies.isEmpty());
    }

    @Test
    @Order(5)
    void testClearAllCookies() {
        Cookie cookie1 = new Cookie("cookie1", "value1");
        Cookie cookie2 = new Cookie("cookie2", "value2");
        cookieManager.addCookie(TEST_DOMAIN, cookie1);
        cookieManager.addCookie("other.com", cookie2);

        cookieManager.clearAllCookies();
        assertTrue(cookieManager.getCookies(TEST_DOMAIN).isEmpty());
        assertTrue(cookieManager.getCookies("other.com").isEmpty());
    }

    @Test
    @Order(6)
    void testPersistence() throws Exception {
        Cookie cookie = new Cookie("persist_test", "persist_value");
        cookieManager.addCookie(TEST_DOMAIN, cookie);
        
        // Crear un nuevo CookieManager para verificar la carga desde el archivo
        cookieManager.shutdown();
        CookieManager newManager = new CookieManager();
        
        Set<Cookie> cookies = newManager.getCookies(TEST_DOMAIN);
        assertEquals(1, cookies.size());
        Cookie loadedCookie = cookies.iterator().next();
        assertEquals("persist_test", loadedCookie.getName());
        assertEquals("persist_value", loadedCookie.getValue());
        
        newManager.shutdown();
    }

    @Test
    @Order(7)
    void testMultipleCookiesPerDomain() {
        Cookie cookie1 = new Cookie("cookie1", "value1");
        Cookie cookie2 = new Cookie("cookie2", "value2");
        Cookie cookie3 = new Cookie("cookie3", "value3");

        cookieManager.addCookie(TEST_DOMAIN, cookie1);
        cookieManager.addCookie(TEST_DOMAIN, cookie2);
        cookieManager.addCookie(TEST_DOMAIN, cookie3);

        Set<Cookie> cookies = cookieManager.getCookies(TEST_DOMAIN);
        assertEquals(3, cookies.size());
    }

    @Test
    @Order(8)
    void testCookieUpdate() {
        Cookie cookie = new Cookie("update_test", "original_value");
        cookieManager.addCookie(TEST_DOMAIN, cookie);

        Cookie updatedCookie = new Cookie("update_test", "new_value");
        cookieManager.addCookie(TEST_DOMAIN, updatedCookie);

        Set<Cookie> cookies = cookieManager.getCookies(TEST_DOMAIN);
        assertEquals(1, cookies.size());
        assertEquals("new_value", cookies.iterator().next().getValue());
    }
} 