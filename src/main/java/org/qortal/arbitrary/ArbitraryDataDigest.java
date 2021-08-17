package org.qortal.arbitrary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qortal.utils.Base58;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArbitraryDataDigest {

    private static final Logger LOGGER = LogManager.getLogger(ArbitraryDataDigest.class);

    private Path path;
    private byte[] hash;

    public ArbitraryDataDigest(Path path) {
        this.path = path;
    }

    public void compute() throws IOException {
        List<Path> allPaths = new ArrayList<>();
        Files.walk(path).filter(Files::isRegularFile).forEachOrdered(p -> allPaths.add(p));
        Path basePathAbsolute = this.path.toAbsolutePath();

        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 hashing algorithm unavailable");
        }

        for (Path path : allPaths) {
            // We need to work with paths relative to the base path, to ensure the same hash
            // is generated on different systems
            Path relativePath = basePathAbsolute.relativize(path.toAbsolutePath());

            // Exclude Qortal folder since it can be different each time
            // We only care about hashing the actual user data
            if (relativePath.startsWith(".qortal/")) {
                continue;
            }

            // Hash path
            byte[] filePathBytes = relativePath.toString().toLowerCase().getBytes(StandardCharsets.UTF_8);
            sha256.update(filePathBytes);

            // Hash contents
            byte[] fileContent = Files.readAllBytes(path);
            sha256.update(fileContent);
        }
        this.hash = sha256.digest();
    }

    public boolean isHashValid(byte[] hash) {
        return Arrays.equals(hash, this.hash);
    }

    public byte[] getHash() {
        return this.hash;
    }

    public String getHash58() {
        if (this.hash == null) {
            return null;
        }
        return Base58.encode(this.hash);
    }

}
