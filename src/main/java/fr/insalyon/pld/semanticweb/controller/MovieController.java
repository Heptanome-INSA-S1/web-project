package fr.insalyon.pld.semanticweb.controller;

import fr.insalyon.pld.semanticweb.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.MovieRepository;
import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @RequestMapping(value = "/movies/{uri}", produces = "application/json")
    public @ResponseBody
    Movie getMovie(@PathVariable String uri) {

        Optional<Movie> filmOptional = movieRepository.findById(uri);

        if(filmOptional.isPresent()) {
            return filmOptional.get();
        } else {
            throw new RuntimeException("Impossible to reach this uri: " + uri + " at the resource path " + SPARQLRepository.defaultResourcePath);
        }

    }

    @RequestMapping("/movies")
    public @ResponseBody
    List<Movie> index(@RequestParam(value = "q", required = false) String query) {

        if (null == query) {
            return movieRepository.findAll();
        } else {
            return movieRepository.findByName(query);
        }
    }

}
