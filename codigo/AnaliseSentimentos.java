import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Aplicação Java para análise de sentimentos usando Azure Cognitive Services
 * Disciplina: Trabalho Interdisciplinar - PUC Minas
 * Aluno: Leonardo Araujo Passos
 * Data: Outubro/2025
 */
public class AnaliseSentimentos {
    // primeiro tenta System.getenv; se ausentes, carrega ./ .env como fallback
    private static String ENDPOINT = System.getenv("ENDPOINT_AZURE");
    private static String API_KEY  = System.getenv("API_KEY_AZURE");
    private static String API_PATH = System.getenv("API_PATH_AZURE");

    static {
        if ((ENDPOINT == null || ENDPOINT.isEmpty()) ||
            (API_KEY  == null || API_KEY.isEmpty())  ||
            (API_PATH == null || API_PATH.isEmpty())) {
            Map<String,String> env = loadDotEnv(".env");
            if ((ENDPOINT == null || ENDPOINT.isEmpty())) {
                String v = env.get("ENDPOINT_AZURE");
                if (v != null && !v.isEmpty()) ENDPOINT = v;
            }
            if ((API_KEY == null || API_KEY.isEmpty())) {
                String v = env.get("API_KEY_AZURE");
                if (v != null && !v.isEmpty()) API_KEY = v;
            }
            if ((API_PATH == null || API_PATH.isEmpty())) {
                String v = env.get("API_PATH_AZURE");
                if (v != null && !v.isEmpty()) API_PATH = v;
            }
        }
    }

    private static Map<String,String> loadDotEnv(String path) {
        Map<String,String> map = new HashMap<>();
        File f = new File(path);
        if (!f.exists() || !f.isFile()) return map;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;
                int eq = linha.indexOf('=');
                if (eq <= 0) continue;
                String chave = linha.substring(0, eq).trim();
                String valor = linha.substring(eq + 1).trim();
                valor = stripQuotes(valor);
                map.put(chave, valor);
            }
        } catch (IOException e) {
            // ignorar erros de leitura; retornar map possivelmente vazio
        }
        return map;
    }

    private static String stripQuotes(String s) {
        if (s == null) return null;
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
    
    public static void main(String[] args) {
        try {
            // Validação das variáveis (agora considera .env como fallback)
            if (ENDPOINT == null || ENDPOINT.isEmpty() ||
                API_KEY == null || API_KEY.isEmpty() ||
                API_PATH == null || API_PATH.isEmpty()) {
                System.err.println("Erro: defina ENDPOINT_AZURE, API_KEY_AZURE e API_PATH_AZURE (variáveis de ambiente ou ./ .env).");
                return;
            }
            
            // Normalizar barras entre endpoint e path
            String ep = ENDPOINT.endsWith("/") ? ENDPOINT.substring(0, ENDPOINT.length() - 1) : ENDPOINT;
            String path = API_PATH.startsWith("/") ? API_PATH : ("/" + API_PATH);
            // Substituir uso direto por ep e path quando necessário mais abaixo

            // Textos de exemplo para análise
            String[] textos = {
                    "Eu amo programar em Java! É incrível trabalhar com Azure.",
                    "Estou muito frustrado com este bug. Nada está funcionando.",
                    "O clima está nublado hoje.",
                    "A PUC Minas oferece excelentes cursos de tecnologia!"
            };

            System.out.println("=== ANÁLISE DE SENTIMENTOS - AZURE AI ===\n");

            // Analisar cada texto
            for (int i = 0; i < textos.length; i++) {
                System.out.println("Texto " + (i + 1) + ": " + textos[i]);
                String resultado = analisarSentimento(textos[i], ep, path);
                System.out.println("Resultado: " + resultado);
                System.out.println("-------------------------------------------\n");
            }

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envia texto para Azure e retorna análise de sentimento
     */
    private static String analisarSentimento(String texto, String endpoint, String apiPath) throws Exception {
        // Construir URL completa
        String urlCompleta = endpoint + apiPath;
        URL url = new URL(urlCompleta);

        // Criar conexão HTTP
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("POST");
        conexao.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setDoOutput(true);

        // Montar JSON de requisição
        JSONObject documento = new JSONObject();
        documento.put("id", "1");
        documento.put("language", "pt");
        documento.put("text", texto);

        JSONArray documentos = new JSONArray();
        documentos.put(documento);

        JSONObject corpo = new JSONObject();
        corpo.put("documents", documentos);

        // Enviar requisição
        DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
        byte[] dados = corpo.toString().getBytes("UTF-8");
        saida.write(dados);
        saida.flush();
        saida.close();

        // Ler resposta
        int codigoResposta = conexao.getResponseCode();

        if (codigoResposta == 200) {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream()));

            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = entrada.readLine()) != null) {
                resposta.append(linha);
            }
            entrada.close();

            // Processar resposta JSON
            return processarResposta(resposta.toString());

        } else {
            // Ler mensagem de erro detalhada
            BufferedReader erroReader = new BufferedReader(
                    new InputStreamReader(conexao.getErrorStream()));
            StringBuilder erroMsg = new StringBuilder();
            String linha;
            while ((linha = erroReader.readLine()) != null) {
                erroMsg.append(linha);
            }
            erroReader.close();
            return "Erro " + codigoResposta + ": " + erroMsg.toString();
        }
    }

    /**
     * Processa JSON de resposta e extrai sentimento
     */
    private static String processarResposta(String jsonResposta) {
        try {
            JSONObject resposta = new JSONObject(jsonResposta);
            JSONArray documentos = resposta.getJSONArray("documents");
            JSONObject documento = documentos.getJSONObject(0);

            String sentimento = documento.getString("sentiment");
            JSONObject scores = documento.getJSONObject("confidenceScores");

            double positivo = scores.getDouble("positive");
            double neutro = scores.getDouble("neutral");
            double negativo = scores.getDouble("negative");

            // Traduzir sentimento
            String sentimentoTraduzido = traduzirSentimento(sentimento);

            return String.format("%s (Positivo: %.2f%%, Neutro: %.2f%%, Negativo: %.2f%%)",
                    sentimentoTraduzido, positivo * 100, neutro * 100, negativo * 100);

        } catch (Exception e) {
            return "Erro ao processar resposta: " + e.getMessage();
        }
    }

    /**
     * Traduz sentimento de inglês para português
     */
    private static String traduzirSentimento(String sentimento) {
        switch (sentimento.toLowerCase()) {
            case "positive":
                return "POSITIVO ✓";
            case "negative":
                return "NEGATIVO ✗";
            case "neutral":
                return "NEUTRO ○";
            case "mixed":
                return "MISTO ◐";
            default:
                return sentimento;
        }
    }
}