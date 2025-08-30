package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    private LocalFileRepository repo;

    @Override
    public void init() {
        repo = LocalFileRepository.createDefault();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String rel = req.getParameter("path");
        if (rel == null || rel.isBlank() || rel.contains("..")) {
            resp.sendError(400, "path inválido");
            return;
        }

        // Borrar Archivo
        repo.delete(rel);

        // Preservar la pagina y paginacion
        StringBuilder redirectUrl = new StringBuilder(req.getContextPath() + "/list");
        String page = req.getParameter("page");
        String size = req.getParameter("size");
        boolean hasParams = false;

        if (page != null && !page.isBlank()) {
            redirectUrl.append("?page=").append(page);
            hasParams = true;
        }
        if (size != null && !size.isBlank()) {
            redirectUrl.append(hasParams ? "&" : "?").append("size=").append(size);
        }

        // Redirigir a /list con los parametros preservados
        resp.sendRedirect(redirectUrl.toString());
    }
}