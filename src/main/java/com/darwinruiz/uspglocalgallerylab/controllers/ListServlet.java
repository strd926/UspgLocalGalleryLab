package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private LocalFileRepository repo;

    @Override
    public void init() {
        repo = LocalFileRepository.createDefault();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Obtener todas las imagenes
        List<String> all = repo.listByExtensionsRecursive("imagenes", ".png", ".jpg", ".jpeg", ".gif", ".webp");


        int page = 1;
        int size = 12;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                page = Math.max(1, Integer.parseInt(pageParam));
            }
            String sizeParam = req.getParameter("size");
            if (sizeParam != null) {
                size = Integer.parseInt(sizeParam);
                size = Math.max(1, size); // tamanio almenos 1
            }
        } catch (NumberFormatException e) {

        }

        // Calculo del indice de paginacioens
        int total = all.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);


        fromIndex = Math.max(0, Math.min(fromIndex, total));
        toIndex = Math.max(0, Math.min(toIndex, total));

        List<String> pageItems = all.subList(fromIndex, toIndex);

        req.setAttribute("localImages", pageItems);
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("total", total);

        req.getRequestDispatcher("/gallery.jsp").forward(req, resp);
    }
}