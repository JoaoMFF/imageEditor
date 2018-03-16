package pt.ipbeja.eda;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Joao_
 * Fonte de inspiração André Lascas
 */
public class KernelDensityEstimation {

	BufferedImage img;
	File f = null; // Imagem
	int width;
	int height;
	int nPixels;// Altura e Largura da imagem
	int[] grayValues;
	int counter;
	int total;
	double average = pixelValues();
	double sDev = standartDeviation(average);
	double bandWidth = bandWidth(sDev);
	double[] result = new double[256];

	public static void main(String[] args)
	{
			KernelDensityEstimation dens = new KernelDensityEstimation();
			dens.loadImageGrayScale();
			dens.pixelValues();
			dens.run();
	}

	public void loadImageGrayScale()
	{
		try
	    {
	    	this.f = new File("./GrayScale.jpg"); // Nome e diretoria do ficheiro a ser carregado
	    	this.img = ImageIO.read(f);
	    }
		catch(IOException e){
	      System.out.println(e);
		}
	}

	public double pixelValues()
	{
	    this.width = img.getWidth(); // Obtem a largura da imagem carregada
	    this.height = img.getHeight(); // Obtem a altura da imagem carregada
	    this.nPixels = width * height; // Calcula o numero total de pixels da imagem

	    this.grayValues = new int[nPixels]; // Inicializa o array com a dimensão igual ao numero total de pixels da imagem

	    for( int x = 0; x < width; x++)
	    {
	    	for(int y = 0; y < height; y++)
	    	{
	    		this.grayValues[counter] = img.getRGB(x,y); // Preenche o array com o valor RGB de cada pixel da imagem
	    		this.total += this.grayValues[counter];
	    	    this.counter++;
	    	}
	    }
	    return average = this.total / this.nPixels;

	}

	public double standartDeviation(double average)
	{
		double deviation = 0;
		for (int i = 0; i < this.grayValues.length; i++)
		{
			deviation += Math.pow(this.grayValues[i] - average, 2);
		}
		return Math.sqrt(deviation/this.grayValues.length);
	}


	private double bandWidth(double deviation) {

		return 1.06 * deviation * Math.pow(this.grayValues.length, -1.0 / 5.0);
	}

    private double kernel(double u) {

        return (1.0 / (Math.sqrt(2.0) * Math.PI)) * (Math.exp(-0.5 * (u * u)));
    }

    public double kde(double x)
    {
    	double y = 0;
    	for (int i = 0; i < this.grayValues.length; i++)
    	{
    		y += kernel(((x - this.grayValues[i]) / this.bandWidth));
    	}
    	y /= grayValues.length * this.bandWidth;
    	return y;
    }

    public void run()
    {
    	for (int i = 0; i < this.result.length; i++)
    	{
    		this.result[i] = this.kde(i);
    	}
    }

}