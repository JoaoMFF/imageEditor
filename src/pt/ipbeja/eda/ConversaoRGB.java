package pt.ipbeja.eda;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ConversaoRGB
{
	BufferedImage img;
	File f = null;
	int width;
	int height;
	int nPixels;
	int[] rgbValues;
	int[] grayValues;

	int counter = 0;

	public static void main(String[] args)
	{
		ConversaoRGB rgb = new ConversaoRGB();
		rgb.loadImageColor(); // Carregamento da Imagem a cores
		rgb.pixelValues(); // Preenchimento de um array com o valor de cada pixel da imagem
		long startTime_rgb = System.currentTimeMillis(); // Inicia a contagem do tempo de execução
		rgb.run(); // Método run responsável por calcular o valor em grayscale de cada pixel da imagem
		long endTime_rgb = System.currentTimeMillis(); // Termina a contagem do tempo de execução
		long time = endTime_rgb - startTime_rgb;
		System.out.println("The grayscale conversion took " + time + " milliseconds");
		rgb.writeImage(); // Escrita da imagem em Grayscale
	}

	/*
	 * Método responsável por carregar a imagem "Castle.jpg", Imagem a cores fornecida pelo docente
	 * https://www.dyclassroom.com/image-processing-project/how-to-convert-a-color-image-into-grayscale-image-in-java
	 */
	public void loadImageColor()
	{
	    try
	    {
	    	this.f = new File("./Castle.jpg"); // Nome e diretoria do ficheiro a ser carregado
	    	this.img = ImageIO.read(f);
	    }
		catch(IOException e){
	      System.out.println(e);
		}
	}

	/*
	 * Método responsável por preencher o array rgbValues[] com o valor RGB de cada pixel
	 */
	public void pixelValues()
	{
	    this.width = img.getWidth(); // Obtem a largura da imagem carregada
	    this.height = img.getHeight(); // Obtem a altura da imagem carregada
	    this.nPixels = width * height; // Calcula o numero total de pixels da imagem

	    this.rgbValues = new int[nPixels]; // Inicializa o array com a dimensão igual ao numero total de pixels da imagem

	    for( int x = 0; x < width; x++)
	    {
	    	for(int y = 0; y < height; y++)
	    	{
	    		this.rgbValues[counter] = img.getRGB(x,y); // Preenche o array com o valor RGB de cada pixel da imagem

	    	    this.counter++;
	    	}
	    }
	    this.counter = 0;
	    this.grayValues = new int[this.rgbValues.length]; // Inicialização do array grayValues com a mesma dimensão do array rgbValues
	}

	/*
	 * Método responsável por correr o algoritmo de conversão para grayscale
	 */
	public void run()
	{


		for(int x = 0; x < this.rgbValues.length; x++)
		{
			Color p = new Color(this.rgbValues[x]); // Inicialização do construtor da class Color, responsável por encapsular os valores das cores no array rgbValues

		    int r = p.getRed(); // Obtem o valor da cor Vermelha

		    int g = p.getGreen(); // Obtem o valor da cor Verde

		    int b = p.getBlue(); // Obtem o valor da cor Azul

		    // Multiplicação dos valores R G B obtidos pela matriz, 3x3 por 3x1, Obtendo o valor linear de cada cor
		    double rLinear = (int) (r*3.2406)+(g*-1.5372)+(b*-0.4986);
		    double gLinear = (int) (r*-0.9689)+(g*1.8758)+(b*0.0415);
		    double bLinear = (int) (r*0.0557)+(g*-0.2040)+(b*1.0570);

		    int yLinear = (int) (0.2126*rLinear + 0.7152*gLinear + 0.0722*bLinear); // Soma do novo valor do pixel

		    int rgb = ((yLinear&0x0ff)<<16) + ((yLinear&0x0ff)<<8) + (yLinear&0x0ff); // Normalização do valor RGB
		    this.grayValues[x] = rgb; // Preenchimento do array grayValues com o novo valor do pixel em grayscale
		}
	}

	/*
	 *  Método responsável por adicionar à imagem os novos valores dos pixels assim por escrever o ficheiro .jpg
	 */
	public void writeImage()
	{
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				img.setRGB(x, y, this.grayValues[counter]); // Coloca na imagem os novos valores dos pixels, Grayscale

				counter ++;
			}
		}

		try
		{
		this.f = new File("./GrayScale.jpg"); // Nome e diretoria do novo ficheiro
		ImageIO.write(img, "jpg", f); // Escreve o novo ficheiro .jpg
		}

		catch(IOException e){
	      System.out.println(e);
	    }
	}
}

