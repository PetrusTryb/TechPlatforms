package com.trybisz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Path source = Path.of("images");
        Path destination = source.resolve("output");
        try {
            if (!Files.exists(source)) {
                Files.createDirectory(source);
                downloadImages(source, 50);
            }
            if (!Files.exists(destination))
                    Files.createDirectory(destination);
        } catch (IOException e) {
            System.err.println("Cannot create directory" + e.getMessage());
            return;
        }
        processImages(source);
    }

    public static void processImages(Path source) {
        List<Path> files;
        try(Stream<Path> stream = Files.list(source).filter(file->file.toString().endsWith(".jpg")))
        {
            files = stream.toList();
        } catch (IOException e) {
            System.err.println("Cannot list files in directory");
            return;
        }
        try (ForkJoinPool pool = new ForkJoinPool(8)) {
            pool.submit(() -> files.parallelStream().map(file-> {
                try {
                    return new AbstractMap.SimpleEntry<>(file.getFileName(), ImageIO.read(file.toFile()));
                } catch (IOException e) {
                    System.err.println("Cannot read image "+file.getFileName());
                } return null;
            }).filter(Objects::nonNull).map(img-> new AbstractMap.SimpleEntry<>(img.getKey(), transformImage(img.getValue())))
                    .forEach(img->{
                        try {
                            ImageIO.write(img.getValue(), "jpg", source.resolve("output").resolve(img.getKey()).toFile());
                        } catch (IOException e) {
                            System.err.println("Cannot write image "+img.getKey());
                        }
                    }));
            pool.shutdown();
        } catch (Exception e) {
            System.err.println("Cannot create pool");
        }
    }

    public static BufferedImage transformImage(BufferedImage original){
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for(int x = 0; x < original.getWidth(); x++){
            for(int y = 0; y < original.getHeight(); y++){
                int rgb = original.getRGB(x, y);
                Color color = new Color(rgb);
                Color outColor = new Color(color.getRed(), color.getBlue(), color.getGreen());
                image.setRGB(x, y, outColor.getRGB());
            }
        }
        return image;
    }

    public static void downloadImages(Path directory, int count){
        URI endpoint = URI.create("https://picsum.photos/1920/1080");
        for(int i = 1; i <= count; i++){
            Path target = directory.resolve("image" + i + ".jpg");
            try {
                Files.copy(endpoint.toURL().openStream(), target);
                System.out.print("░"+"▓".repeat(i)+"▒".repeat(count-i)+"░\r");
            } catch (Exception e) {
                System.err.println("Cannot download image #"+i);
            }
        }
    }
}
