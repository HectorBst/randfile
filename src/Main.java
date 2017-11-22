import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Bst
 */
public class Main {

	private static int NB_LINES = 20000000;
	private static int LINE_MIN = 50;
	private static int LINE_MAX = 200;
	private static int CHAR_MIN = ' ';
	private static int CHAR_MAX = '~';
	private static volatile int nb = 0;

	public static void main(String... args) throws IOException {
		Files.deleteIfExists(Paths.get("file.txt"));
		FileOutputStream output = new FileOutputStream("file.txt", true);
		final Random random = new Random();

		long before = System.nanoTime();

		random.ints(NB_LINES, LINE_MIN, LINE_MAX + 2)
				.mapToObj(byte[]::new)
				.peek(bytes -> {
					IntStream.range(0, bytes.length - 1)
							.forEach(i -> bytes[i] = (byte) (random.nextInt(CHAR_MAX - CHAR_MIN + 1) + CHAR_MIN));
					bytes[bytes.length - 1] = '\n';
				})
				.peek(bytes -> {
					try {
						output.write(bytes);
					} catch (IOException e) {
						e.printStackTrace(System.out);
						e.printStackTrace(System.err);
					}
				})
				.forEach(bytes -> {
					nb++;
					if (nb % 10000 == 0) {
						System.out.println(nb);
					}
				});

		long after = System.nanoTime();
		double total = after - before;
		total /= 1000000000;
		System.out.println(String.format("\nTook %.3f seconds", total));

		output.close();
	}
}
