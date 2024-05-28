import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import javax.imageio.ImageIO;

final public class ResourceLoader {
	
	public static Image loadImage(String path) throws IOException
	{
		//return Toolkit.getDefaultToolkit().getImage("res/images/"+path);
		return ImageIO.read(ResourceLoader.class.getResourceAsStream("images/"+path));
	}
}