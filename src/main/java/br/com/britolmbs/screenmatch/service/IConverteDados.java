package br.com.britolmbs.screenmatch.service;

public interface IConverteDados {
   <T> T obterDados(String json, Class<T> classe);
}
