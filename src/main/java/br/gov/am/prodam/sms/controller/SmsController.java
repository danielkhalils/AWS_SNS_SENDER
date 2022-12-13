package br.gov.am.prodam.sms.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
/**
 * Created by anuragdhunna
 */
@RestController
public class SmsController {
	
	@Autowired
	public static void main(String[] args) {
		// Your Credentials
		String ACCESS_KEY = "";
		String SECRET_KEY = "";
		String topicName = "";
		String message = "Teste de mensagem";
		// Adicionando telefones a lista de numeros
		List<String> phoneNumbers = Arrays.asList("");// Ex: +919384374XX
		AmazonSNSClient snsClient = new AmazonSNSClient(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));

		// Criar um topico SMS
		String topicArn = createSNSTopic(snsClient, topicName);

		// Adicionar numeros de telefone ao topico
		subscribeToTopic(snsClient, topicArn, "sms", phoneNumbers);

		// Publicar mensagem ao topico
		sendSMSMessageToTopic(snsClient, topicArn, message);
	}
	public static String createSNSTopic(AmazonSNSClient snsClient,
										String topicName) {
		CreateTopicRequest createTopic = new
				CreateTopicRequest(topicName);
		CreateTopicResult result =
				snsClient.createTopic(createTopic);
		return result.getTopicArn();
	}
	public static void subscribeToTopic(AmazonSNSClient snsClient, String topicArn,
										String protocol, List<String> phoneNumbers) {
		for (String phoneNumber : phoneNumbers) {
			SubscribeRequest subscribe = new SubscribeRequest(topicArn, protocol, phoneNumber);
			snsClient.subscribe(subscribe);
		}
	}
	public static String sendSMSMessageToTopic(AmazonSNSClient snsClient, String topicArn, String message) {
		PublishResult result = snsClient.publish(new PublishRequest()
				.withTopicArn(topicArn)
				.withMessage(message));
		return result.getMessageId();
	}
}