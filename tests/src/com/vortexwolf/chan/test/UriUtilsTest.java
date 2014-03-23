package com.vortexwolf.chan.test;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.vortexwolf.chan.common.utils.UriUtils;

public class UriUtilsTest extends InstrumentationTestCase {

    private static final String imageUri = "http://2-ch.so/b/src/12345.png";
    private static final String boardUriVg = "http://2-ch.so/vg";
    private static final String boardUriBPage1 = "http://2-ch.so/b/1.html";
    private static final String threadRelativeUriB = "/b/res/1000.html";
    private static final String threadUriB = "http://2-ch.so/b/res/1000.html";
    private static final String postUriPr = "http://2-ch.so/pr/res/2000.html#222";

    public void testGetBoardName() {

        Uri uri = Uri.parse(boardUriVg);
        String boardName = UriUtils.getBoardName(uri);
        assertEquals(boardName, "vg");

        uri = Uri.parse(boardUriVg + "/");
        boardName = UriUtils.getBoardName(uri);
        assertEquals(boardName, "vg");

        uri = Uri.parse(boardUriBPage1);
        boardName = UriUtils.getBoardName(uri);
        assertEquals(boardName, "b");

        uri = Uri.parse(threadUriB);
        boardName = UriUtils.getBoardName(uri);
        assertEquals(boardName, "b");

        uri = Uri.parse(threadRelativeUriB);
        boardName = UriUtils.getBoardName(uri);
        assertEquals(boardName, "b");

        uri = Uri.parse(postUriPr);
        boardName = UriUtils.getBoardName(uri);
        assertEquals(boardName, "pr");
    }

    public void testGetThreadNumber() {
        Uri uri = Uri.parse(threadUriB);
        String pageName = UriUtils.getThreadNumber(uri);
        assertEquals(pageName, "1000");

        uri = Uri.parse(threadRelativeUriB);
        pageName = UriUtils.getThreadNumber(uri);
        assertEquals(pageName, "1000");

        uri = Uri.parse(postUriPr);
        pageName = UriUtils.getThreadNumber(uri);
        assertEquals(pageName, "2000");
    }

    public void testGetBoardPageNumber() {
        Uri uri = Uri.parse(boardUriBPage1);
        int pageNumber = UriUtils.getBoardPageNumber(uri);
        assertEquals(pageNumber, 1);

        uri = Uri.parse(boardUriVg);
        pageNumber = UriUtils.getBoardPageNumber(uri);
        assertEquals(pageNumber, 0);
    }

    public void testIsYoutubeUri() {
        Uri uri = Uri.parse("http://youtube.com/watch?v=F4hQ4J4BFOM");
        assertTrue(UriUtils.isYoutubeUri(uri));
    }

    public void testIsImageUri() {
        Uri uri = Uri.parse(imageUri);
        assertEquals(UriUtils.isImageUri(uri), true);

        uri = Uri.parse(threadUriB);
        assertEquals(UriUtils.isImageUri(uri), false);
    }

    public void testGetFileExtension() {
        Uri uri = Uri.parse("/test/1 2 3.pdf");
        String extension = UriUtils.getFileExtension(uri);
        assertEquals(extension, "pdf");

        uri = Uri.parse("src/test.flash.swf");
        extension = UriUtils.getFileExtension(uri);
        assertEquals(extension, "swf");

        uri = Uri.parse(imageUri);
        extension = UriUtils.getFileExtension(uri);
        assertEquals(extension, "png");

        uri = Uri.parse(threadUriB);
        extension = UriUtils.getFileExtension(uri);
        assertEquals(extension, "html");

        uri = Uri.parse(boardUriVg);
        extension = UriUtils.getFileExtension(uri);
        assertEquals(extension, null);
    }

    public void testIsThreadUri() {
        assertEquals(UriUtils.isThreadUri(Uri.parse(threadUriB)), true);

        assertEquals(UriUtils.isThreadUri(Uri.parse(postUriPr)), true);

        assertEquals(UriUtils.isThreadUri(Uri.parse(boardUriVg)), false);

        assertEquals(UriUtils.isThreadUri(Uri.parse(imageUri)), false);
    }

    public void testIsBoardUri() {

        assertEquals(UriUtils.isBoardUri(Uri.parse(boardUriVg)), true);

        assertEquals(UriUtils.isBoardUri(Uri.parse(boardUriBPage1)), true);

        assertEquals(UriUtils.isBoardUri(Uri.parse(threadUriB)), false);

        assertEquals(UriUtils.isBoardUri(Uri.parse(imageUri)), false);
    }

    public void testGetYoutubeCodeTest() {
        String testHtml = "<a href=\"http://www.youtube.com/v/F4hQ4J4BFOM\"/>";
        String code = UriUtils.getYouTubeCode(testHtml);
        assertEquals(code, "F4hQ4J4BFOM");

        testHtml = "http://youtube.com/watch?v=_-123ZZZzzz";
        code = UriUtils.getYouTubeCode(testHtml);
        assertEquals(code, "_-123ZZZzzz");

        testHtml = "https://www.youtube.com/watch?v=1-123ZZZzzz";
        code = UriUtils.getYouTubeCode(testHtml);
        assertEquals(code, "1-123ZZZzzz");

        testHtml = "http://m.youtube.com/watch?v=123456789zz";
        code = UriUtils.getYouTubeCode(testHtml);
        assertEquals(code, "123456789zz");

        testHtml = "http://m.youtube.com/#/watch?v=123456789xx";
        code = UriUtils.getYouTubeCode(testHtml);
        assertEquals(code, "123456789xx");

    }
}
