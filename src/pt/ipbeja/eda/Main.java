package pt.ipbeja.eda;

public class Main
{
	public static void main(String[] args)
	{
		// Inicializa��o do requisito 2.1
		/*ConversaoRGB rgb = new ConversaoRGB();
		rgb.loadImageColor(); // Carregamento da Imagem a cores
		rgb.pixelValues(); // Preenchimento de um array com o valor de cada pixel da imagem
		long startTime_rgb = System.currentTimeMillis(); // Inicia a contagem do tempo de execu��o
		rgb.run(); // M�todo run respons�vel por calcular o valor em grayscale de cada pixel da imagem
		long endTime_rgb = System.currentTimeMillis(); // Termina a contagem do tempo de execu��o
		System.out.println("The grayscale conversion took " + (endTime_rgb - startTime_rgb) + " milliseconds");
		rgb.writeImage();*/ // Escrita da imagem em Grayscale

		// Inicializa��o do requisito 2.2
		GaussFilter gauss = new GaussFilter();
		/*gauss.loadImageGrayScale(); // Carregamento da Imagem em grayscale criada do requisito 2.1
		gauss.gaussNoise();	// Adi��o de Gaussian Noise � imagem em Grayscale
		gauss.writeImageNoise();*/ // Escrita da imagem com Gaussian Noise
		gauss.loadImageNoise(); // Carregamento da imagem com Gaussian Noise
		gauss.pixelValue(); // Preenchimento de um array com o valor de cada pixel da imagem
		gauss.createKernel(); // Cria��o da matriz kernel de dimens�o 7
		long startTime_gauss = System.currentTimeMillis(); // Inicia a contagem do tempo de execu��o
		gauss.run(); // M�todo run respons�vel por correr a convuls�o horizontal e vertical
		long endTime_gauss = System.currentTimeMillis(); // Termina a contagem do tempo de execu��o
		System.out.println("That took " + (endTime_gauss - startTime_gauss) + " milliseconds");
		gauss.writeImageBlur(); // Escrita da imagem com Gaussian Blur
	}
}
