package pt.ipbeja.eda;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class GaussFilter
{
	BufferedImage img;
	Raster imgRaster;
	WritableRaster writeRaster;
	File f = null;
	Random randGen = new Random();
	int width;
	int height;
	int bands;
	double sigma = 0.84089642;
	double radius = this.sigma * 3;
	double total;
	double gaussian;
	int noiseValue;
	int variance = 30;
	double pixel;
	double[] kernel;
	double[] kernelDivided;
	int[][] rgbValues;
	int kernelSize = 3;
	int index = 0;

	public static void main(String[] args)
	{
			GaussFilter gauss = new GaussFilter();
			gauss.loadImageGrayScale(); // Carregamento da Imagem em grayscale criada do requisito 2.1
			gauss.gaussNoise();	// Adi��o de Gaussian Noise � imagem em Grayscale
			gauss.writeImageNoise(); // Escrita da imagem com Gaussian Noise
			gauss.loadImageNoise(); // Carregamento da imagem com Gaussian Noise
			gauss.pixelValue(); // Preenchimento de um array com o valor de cada pixel da imagem
			gauss.createKernel(); // Cria��o da matriz kernel de dimens�o 7
			long startTime_gauss = System.currentTimeMillis(); // Inicia a contagem do tempo de execu��o
			gauss.run(); // M�todo run respons�vel por correr a convuls�o horizontal e vertical
			long endTime_gauss = System.currentTimeMillis();
			long time = startTime_gauss - endTime_gauss;// Termina a contagem do tempo de execu��o
			System.out.println("That took " + time + " milliseconds");
			gauss.writeImageBlur();
	}

	/*
	 * M�todo respons�vel por carregar a imagem "GrayScale.jpg" criada no requisito 2.1
	 * https://www.dyclassroom.com/image-processing-project/how-to-convert-a-color-image-into-grayscale-image-in-java
	 */
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

	/*
	 * M�todo respons�vel por adicionar Gaussian Noise � imagem carregada
	 * https://www.codemiles.com/java-examples/image-noise-filter-in-java-t10946.html
	 */
	public void gaussNoise()
	{
	    this.imgRaster = img.getRaster(); // Inicializa o Raster, reponsavel por obter a  informa��o do pixel
	    this.writeRaster = img.getRaster(); // Inicializa o WritableRaster, respons�vel por garantir a possibilidade de escrever a informa��o dos pixels

	    this.width = img.getWidth(); // Obtem a largura da imagem carregada
	    this.height = img.getHeight(); // Obtem a altura da imagem carregada
	    this.bands = writeRaster.getNumBands(); // Obtem o numero de bandas no Raster
	    for(int y = 0; y < height; y++)
	    {
	    	for(int x = 0; x < width; x++)
	    	{
	    		this.gaussian = randGen.nextGaussian(); // Retorna o pr�ximo valor de distribui��o Gaussiana

	    		for (int z = 0; z < bands; z++)
	    		{
	    			this.pixel = imgRaster.getSample(x,y,z); // Obtem a sample de uma banda especifica do pixel

	    			this.noiseValue = (int) (this.gaussian * variance); // Multiplica��o do desvio padr�o (30) por o valor obtido a partir do nextGaussian();
	    			this.noiseValue = (int) (this.noiseValue + this.pixel); // Adi��o do valor obtido da multiplica��o ao valor do pixel

	    			// Caso o novo valor do pixel seja < 0 o novo valor ter� o valor de 0, Caso seja > 255 o valor ser� 255
	    			if(this.noiseValue < 0)
	    			{
	    				this.noiseValue = 0;
	    			}
	    			if(this.noiseValue > 255)
	    			{
	    				this.noiseValue = 255;
	    			}
	    			this.writeRaster.setSample(x, y, z, this.noiseValue); // Coloca na Sample o novo valor do pixel, Pixel com ruido
	    		}
	    	}
	    }
	}

	/*
	 * M�todo respons�vel por guardar a imagem com ruido gaussiano
	 */
	public void writeImageNoise()
	{
		try{
	        f = new File("./GaussianNoise.jpg"); // Nome e destino do novo ficheiro
	        ImageIO.write(img, "jpg", f);
	      }catch(IOException e){
	        System.out.println(e);
	      }
	}

	/*
	 * M�todo respons�vel por carregar a imagem "GaussianNoise.jpg", imagem criada previamente
	 */
	public void loadImageNoise()
	{
	    try
	    {
	    	this.f = new File("./GaussianNoise.jpg"); // Ficheiro a ser carregado
	    	this.img = ImageIO.read(f);
	    }
		catch(IOException e){
	      System.out.println(e);
		}
	}

	/*
	 * Cria��o do kernel, Matriz gaussiana
	 * http://www.pixelstech.net/article/1353768112-Gaussian-Blur-Algorithm
	 * https://www.blitzbasic.com/Community/posts.php?topic=84166
	 */
	public void createKernel()
	{
		int radiusCeil = (int) Math.ceil(this.radius); // Radius � 3 * o valor do sigma (matriz de 7 posi��es), Arredondamento do valor do Radius para cima
		int rows = radiusCeil * 2 + 1; // Numero de posi��es da Matriz
		this.kernel = new double[rows]; // Inicializa��o do array kernel com o numero de posi��es "rows" (7)
		this.kernelDivided = new double[rows]; // Inicializa��o do array kernelDivided com o numero de posi��es "rows" (7)
		this.sigma = this.radius / 3; // Aproxima��o de Valores
		double normalization = 1.0 / (Math.sqrt(2 * Math.PI * (this.sigma * this.sigma))); // Primeira parte da formula de distribui��o
		double denominator = 2.0 * (this.sigma * this.sigma); //Denominador da segunda parte da formula da distribui��o

		// Ciclo for que vai desde -3 a 3
		for (int x = - radiusCeil; x <= radiusCeil; x++)
		{
			this.kernel[this.index] = normalization * Math.exp( -(x * x) / denominator); // Calculo da distribui��o e adi��o desses valores �s posi��es do Array kernel
			this.total += this.kernel[this.index]; // Calculo da soma de todos os valores do array kernel
			this.index++;
		}

		for (int x = 0; x < this.kernel.length; x++)
		{
			this.kernelDivided[x] = this.kernel[x]/total; // Normaliza��o da Matriz, dividindo cada valor da mesma por o total da soma de todos os valores.
			// A soma de todos os valores do novo array dever� ser ~1
		}
	}

	/*
	 * M�todo respons�vel por preencher o array rgbValues[] com o valor RGB de cada pixel
	 */
	public void pixelValue()
	{
		this.width = img.getWidth(); // Obtem a largura da imagem carregada
	    this.height = img.getHeight(); // Obtem a altura da imagem carregada
	    this.rgbValues = new int[this.width][this.height]; // Inicializa o array multidimensional com as dimens�es da largura e altura

	    for( int x = 0; x < width; x++)
	    {
	    	for(int y = 0; y < height; y++)
	    	{
	    		this.rgbValues[x][y] = img.getRGB(x,y); // Preenche o array com o valor RGB de cada pixel da imagem
	    	}
	    }
	}

	/*
	 * M�todo respons�vel por efetuar a convuls�o horizontal dos pixels
	 * https://drive.google.com/file/d/0B3tv90xhWPElLWZOYkwwd3lieEk/view
	 */
	public void horizontalConvolution()
	{
		this.width = img.getWidth(); // Obtem a largura da imagem carregada
	    this.height = img.getHeight(); // Obtem a altura da imagem carregada

	    for (int y = 0; y < this.height; y++)
	    {
	    	for (int x = 0; x < this.width; x++)
	    	{
	    		double r = 0; // Inicializa a vari�vel red a 0
	    		double g = 0; // Inicializa a vari�vel green a 0
	    		double b = 0; // Inicializa a vari�vel blue a 0
	    		for (int z = 0; z < this.kernel.length; z++)
	    		{
	    			int line = x - 3 + z; // novo valor de x, prevenir que n�o acontece nada quando a matriz est� fora da frame da imagem
	    			if (line < 0 || line > this.width - 1)
	    			{
	    				//Caso o pixel esteja fora da frame da imagem n�o � feita nenhuma modifica��o
	    			}
	    			else
	    			{
		    			Color p = new Color(this.rgbValues[line][y]); // Inicializa��o do construtor da class Color, respons�vel por encapsular os valores das cores no array rgbValues
		    			r += p.getRed() * this.kernelDivided[z]; // Obtem o valor da cor Vermelha do pixel e multiplica por o valor da matriz
		    			g += p.getGreen() * this.kernelDivided[z]; // Obtem o valor da cor Verde do pixel e multiplica por o valor da matriz
		    			b += p.getBlue() * this.kernelDivided[z]; // Obtem o valor da cor Azul do pixel e multiplica por o valor da matriz
	    			}
	    		}
	    		this.rgbValues[x][y] = new Color((int) r,(int) g,(int) b).getRGB(); // Coloca no array rgbValues os novos valores dos pixels
	    	}
	    }
	}

	/*
	 * M�todo respons�vel por efetuar a convuls�o vertical dos pixels
	 * https://drive.google.com/file/d/0B3tv90xhWPElLWZOYkwwd3lieEk/view
	 */
	public void verticalConvolution()
	{
		this.width = img.getWidth(); // Obtem a largura da imagem carregada
	    this.height = img.getHeight(); // Obtem a altura da imagem carregada

	    for (int y = 0; y < this.height; y++)
	    {
	    	for (int x = 0; x < this.width; x++)
	    	{
	    		double r = 0; // Inicializa a vari�vel red a 0
	    		double g = 0; // Inicializa a vari�vel green a 0
	    		double b = 0; // Inicializa a vari�vel blue a 0
	    		for (int z = 0; z < this.kernel.length; z++)
	    		{
	    			int line = y - 3 + z; // novo valor de y, prevenir que n�o acontece nada quando a matriz est� fora da frame da imagem
	    			if (line < 0 || line > this.height - 1)
	    			{
	    				//Caso o pixel esteja fora da frame da imagem n�o � feita nenhuma modifica��o
	    			}
	    			else
	    			{
	    				Color p = new Color(this.rgbValues[x][line]); // Inicializa��o do construtor da class Color, respons�vel por encapsular os valores das cores no array rgbValues
		    			r += p.getRed() * this.kernelDivided[z]; // Obtem o valor da cor Vermelha do pixel e multiplica por o valor da matriz
		    			g += p.getGreen() * this.kernelDivided[z]; // Obtem o valor da cor Verde do pixel e multiplica por o valor da matriz
		    			b += p.getBlue() * this.kernelDivided[z]; // Obtem o valor da cor Azul do pixel e multiplica por o valor da matriz
	    			}
	    		}
	    		this.rgbValues[x][y] = new Color((int) r,(int) g,(int) b).getRGB(); // Coloca no array rgbValues os novos valores dos pixels
	    	}
	    }
	}

	/*
	 * M�todo respons�vel por inicializar os m�todos horizontalConvolution() e verticalConvolution()
	 */
	public void run()
	{
		this.horizontalConvolution();
		this.verticalConvolution();
	}

	/*
	 * M�todo respons�vel por adicionar � imagem os novos valores dos pixels assim por escrever o ficheiro .jpg
	 */
	public void writeImageBlur()
	{
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				img.setRGB(x, y, this.rgbValues[x][y]); // Coloca na imagem os novos valores dos pixels, blur
			}
		}

		try{
	        f = new File("./GaussianBlur.jpg"); // Nome e diretoria do novo ficheiro
	        ImageIO.write(img, "jpg", f); // Escreve o novo ficheiro .jpg
	      }catch(IOException e){
	        System.out.println(e);
	      }
	}
}
