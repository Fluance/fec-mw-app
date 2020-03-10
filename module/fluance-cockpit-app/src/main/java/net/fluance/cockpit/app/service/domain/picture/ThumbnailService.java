package net.fluance.cockpit.app.service.domain.picture;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ThumbnailService {
	
	private static final Logger LOGGER = LogManager.getLogger(ThumbnailService.class);
	
	/**
	 * Creates a BufferedImage thumbnail that fits into the size of the square which can be defined by parameter.
	 * @param originalFile
	 * @param thumbnailSquareSize
	 * @return Returns the generated BufferedImage thumbnail
	 */
	public BufferedImage createThumbnail(File originalFile, double thumbnailSquareSize) {
		if (originalFile != null && thumbnailSquareSize > 0) {
			try {
				BufferedImage bufferedImage = ImageIO.read(originalFile);
				
				boolean isHeightBiggerThanWidth = isHeightBiggerThanWidth(bufferedImage);
				double currentBiggerSize = isHeightBiggerThanWidth ? bufferedImage.getHeight() : bufferedImage.getWidth();
				double currentSmallerSize = isHeightBiggerThanWidth ? bufferedImage.getWidth() : bufferedImage.getHeight();
				
				double calculatedScale = calculateScalling(thumbnailSquareSize, currentBiggerSize);
				double calculatedSmallerSize = calculateNewSmallerSize(currentSmallerSize, calculatedScale);
	
				double newImageHeight = isHeightBiggerThanWidth ? thumbnailSquareSize : calculatedSmallerSize;
				double newImageWidth = isHeightBiggerThanWidth ? calculatedSmallerSize : thumbnailSquareSize;
				
				return adjustBufferedImageSize(bufferedImage, newImageHeight, newImageWidth);
			} catch (IOException exception) {
				LOGGER.error("Error while creating thumbnail", exception);
			}
		}
		return null;
	}

	protected boolean isHeightBiggerThanWidth(BufferedImage bufferedImage)  {
		return bufferedImage.getHeight() > bufferedImage.getWidth();
	}

	protected double calculateScalling(double thumbnailSquareSize, double biggerSize) {
		return thumbnailSquareSize / biggerSize;
	}

	protected double calculateNewSmallerSize(double currentSmallerSize, double calculatedScallingPercentage) {
		return currentSmallerSize * calculatedScallingPercentage;
	}
	
	protected BufferedImage adjustBufferedImageSize (BufferedImage currentBufferedImage, double newImageHeight, double newImageWidth) {
		Image temporaryImage = currentBufferedImage.getScaledInstance((int) newImageWidth, (int) newImageHeight, Image.SCALE_SMOOTH);
		int type = currentBufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : currentBufferedImage.getType();
        BufferedImage resizedBufferedImage = new BufferedImage((int) newImageWidth, (int) newImageHeight, type);
        
        Graphics2D resizedGraphics2D = resizedBufferedImage.createGraphics();
        // Increase quality if needed at the expense of speed
        resizedGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        resizedGraphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        resizedGraphics2D.drawImage(temporaryImage, 0, 0, null);
        resizedGraphics2D.dispose();
        
        return resizedBufferedImage;
	}
}
