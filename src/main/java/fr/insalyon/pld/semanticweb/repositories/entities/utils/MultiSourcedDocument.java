package fr.insalyon.pld.semanticweb.repositories.entities.utils;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class MultiSourcedDocument extends HashMap<URI.Database, Document> implements Map<URI.Database, Document> {}
