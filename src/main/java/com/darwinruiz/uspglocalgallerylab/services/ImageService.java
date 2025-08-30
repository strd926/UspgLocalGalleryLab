package com.darwinruiz.uspglocalgallerylab.services;

import com.darwinruiz.uspglocalgallerylab.dto.UploadResult;
import com.darwinruiz.uspglocalgallerylab.repositories.IFileRepository;
import com.darwinruiz.uspglocalgallerylab.util.ImageValidator;
import com.darwinruiz.uspglocalgallerylab.util.NamePolicy;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageService {
    private final IFileRepository repo;
    public ImageService(IFileRepository repo) { this.repo = repo; }

    public UploadResult uploadLocalImages(Collection<Part> parts) throws IOException, ServletException {
        int ok = 0, bad = 0;
        List<String> saved = new ArrayList<>();

        for (Part p : parts) {
            // verificar que no sea menor a 0
            if (!"file".equals(p.getName()) || p.getSize() == 0) {
                continue;
            }

            // obtener nombre y normalizar
            String submitted = p.getSubmittedFileName();
            String fileName = Paths.get(submitted).getFileName().toString();
            String normalizedFileName = NamePolicy.normalize(fileName);

            // saltar si el nombre normalizado esta vacio
            if (normalizedFileName.isEmpty()) {
                bad++;
                continue;
            }

            // Validar imagen
            if (!ImageValidator.isValid(p, normalizedFileName)) {
                bad++;
                continue;
            }


            String subdir = NamePolicy.datedSubdir(LocalDate.now());

            // guardar
            try (InputStream in = p.getInputStream()) {
                repo.save(subdir, normalizedFileName, in);
                saved.add(subdir + "/" + normalizedFileName);
                ok++;
            } catch (IOException e) {
                bad++;
            }
        }

        return new UploadResult(ok, bad, saved);
    }
}