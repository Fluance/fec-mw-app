package net.fluance.cockpit.app.service.domain.picture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ThumbnailServiceTest {
	private ThumbnailService thumbnailService;
	@Before
	public void before() {
		thumbnailService = new ThumbnailService();
	}
	
	@Test
	public void createThumbnail_1 () throws IOException {
		BufferedImage originalBufferedImage = createTestBufferedImage(200, 200);
		File originalFile = new File("TestFile.png");
		ImageIO.write(originalBufferedImage, "png", originalFile);
		
		BufferedImage bufferedThumbnail = thumbnailService.createThumbnail(originalFile, 150d);
		Assert.assertTrue("Is image still a square", bufferedThumbnail.getWidth() == bufferedThumbnail.getHeight());
		Assert.assertTrue("Is image height 150", bufferedThumbnail.getHeight() == 150);
		Assert.assertTrue("Is image width 150", bufferedThumbnail.getWidth() == 150);
		originalFile.delete();
	}
	
	@Test
	public void createThumbnail_file_null () throws IOException {
		BufferedImage thumbnailFile = thumbnailService.createThumbnail(null, 150d);
		Assert.assertTrue("Returns null because file is null", thumbnailFile == null);
	}
	
	@Test
	public void createThumbnail_square_size_is_below_or_equal_0 () throws IOException {
		BufferedImage originalBufferedImage = createTestBufferedImage(200, 200);
		File originalFile = new File("TestFile.png");
		ImageIO.write(originalBufferedImage, "png", originalFile);
		BufferedImage thumbnailFile = thumbnailService.createThumbnail(originalFile, -3d);
		Assert.assertTrue("Returns null because square size is -3", thumbnailFile == null);
		originalFile.delete();
	}
	
	@Test
	public void isHeightBiggerThanWidth_isHeightBigger() throws IOException {
		BufferedImage bufferedImage = createTestBufferedImage(300, 200);
		boolean isHeightBiggerThanWidth = thumbnailService.isHeightBiggerThanWidth(bufferedImage);
		Assert.assertTrue("Is height bigger than width?", isHeightBiggerThanWidth);
	}
	
	@Test
	public void isHeightBiggerThanWidth_isWidthBigger() throws IOException {
		BufferedImage bufferedImage = createTestBufferedImage(200, 300);
		boolean isHeightBiggerThanWidth = thumbnailService.isHeightBiggerThanWidth(bufferedImage);
		Assert.assertFalse("Is width bigger than height?", isHeightBiggerThanWidth);
	}
	
	@Test
	public void calculateScalling_100_1000() {
		double size = thumbnailService.calculateScalling(100, 1000d);
		Assert.assertTrue("Is size calculation 0.1?", size == 0.1);
	}
	
	@Test
	public void calculateScalling_60_1200() {
		double size = thumbnailService.calculateScalling(60, 1200d);
		Assert.assertTrue("Is size calculation 0.05?", size == 0.05);
	}
	
	@Test
	public void calculateScalling_150_500() {
		double size = thumbnailService.calculateScalling(150, 500d);
		Assert.assertTrue("Is size calculation 0.3?", size == 0.3);
	}
	
	@Test
	public void calculateNewSmallerSize_200_2() {
		double newCalculatedSize = thumbnailService.calculateNewSmallerSize(200d, 2d);
		Assert.assertTrue("Is new calculated size 400?", newCalculatedSize == 400);
	}
	
	@Test
	public void calculateNewSmallerSize_100_0_7() {
		double newCalculatedSize = thumbnailService.calculateNewSmallerSize(100d, 0.7d);
		Assert.assertTrue("Is new calculated size 70?", newCalculatedSize == 70);
	}
	
	@Test
	public void adjustBufferedImageSize_400_600() throws IOException {
		BufferedImage bufferedImage = createTestBufferedImage(200, 300);
		BufferedImage adjustedBufferedImage = thumbnailService.adjustBufferedImageSize(bufferedImage, 400d, 600d);
		Assert.assertTrue("Is new height of the image 400?", adjustedBufferedImage.getHeight() == 400);
		Assert.assertTrue("Is new width of the image 600?", adjustedBufferedImage.getWidth() == 600);
	}
	
	@Test
	public void adjustBufferedImageSize_420_1025() throws IOException {
		BufferedImage bufferedImage = createTestBufferedImage(800, 12);
		BufferedImage adjustedBufferedImage = thumbnailService.adjustBufferedImageSize(bufferedImage, 420d, 1025d);
		Assert.assertTrue("Is new height of the image 420?", adjustedBufferedImage.getHeight() == 420);
		Assert.assertTrue("Is new width of the image 1025?", adjustedBufferedImage.getWidth() == 1025);
	}
	
	private BufferedImage createTestBufferedImage(int height, int width) throws IOException {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
}
