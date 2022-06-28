package io.aggregator.action;

import io.aggregator.api.StripedSecondApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Empty;

import io.aggregator.TimeTo;
import io.aggregator.action.TransactionTopicConsumerService.TopicTransaction;
import io.aggregator.api.TransactionApi;
import kalix.javasdk.action.ActionCreationContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/aggregator/action/transaction_topic_consumer_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class TransactionTopicConsumerAction extends AbstractTransactionTopicConsumerAction {
  private static final Logger log = LoggerFactory.getLogger(TransactionTopicConsumerAction.class);

  public TransactionTopicConsumerAction(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> transactionFromTopic(TopicTransaction topicTransaction) {
    log.debug("transactionFromTopic: {}", topicTransaction);

//    return effects().forward(components().transaction().createTransaction(
//        TransactionApi.CreateTransactionCommand
//            .newBuilder()
//            .setTransactionId(topicTransaction.getTransactionKey().getTransactionId())
//            .setMerchantId(topicTransaction.getMerchantId())
//            .setServiceCode("service-code-1")
//            .setAccountFrom("account-from-1")
//            .setAccountTo("account-to-1")
//            .setShopId(topicTransaction.getMerchantId())
//            .setTransactionAmount(topicTransaction.getTransactionAmount())
//            .setTransactionTimestamp(TimeTo.now())
//            .build()));


    var timestamp = TimeTo.now();
    var stripe = TimeTo.stripe(topicTransaction.getTransactionKey().getTransactionId());

    return effects().forward(components().stripedSecond().addTransaction(
        StripedSecondApi.AddTransactionCommand
            .newBuilder()
            .setTransactionId(topicTransaction.getTransactionKey().getTransactionId())
            .setMerchantId(topicTransaction.getMerchantId())
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setAmount(topicTransaction.getTransactionAmount())
            .setTimestamp(TimeTo.now())
            .setEpochSecond(timestamp.getSeconds() / 10 * 10)
            .setStripe(stripe)
            .build()));


//    var createTransaction = components().transaction().createTransaction(
//        TransactionApi.CreateTransactionCommand
//            .newBuilder()
//            .setTransactionId(topicTransaction.getTransactionKey().getTransactionId())
//            .setMerchantId(topicTransaction.getMerchantId())
//            .setServiceCode("service-code-1")
//            .setAccountFrom("account-from-1")
//            .setAccountTo("account-to-1")
//            .setShopId(topicTransaction.getMerchantId())
//            .setTransactionAmount(topicTransaction.getTransactionAmount())
//            .setTransactionTimestamp(TimeTo.now())
//            .build()).execute();
//
//    var addTransaction = components().stripedSecond().addTransaction(
//        StripedSecondApi.AddTransactionCommand
//            .newBuilder()
//            .setTransactionId(topicTransaction.getTransactionKey().getTransactionId())
//            .setMerchantId(topicTransaction.getMerchantId())
//            .setServiceCode("service-code-1")
//            .setAccountFrom("account-from-1")
//            .setAccountTo("account-to-1")
//            .setAmount(topicTransaction.getTransactionAmount())
//            .setTimestamp(TimeTo.now())
//            .setEpochSecond(timestamp.getSeconds())
//            .setStripe(stripe)
//            .build()).execute();
//
//    CompletionStage<Empty> result = CompletableFuture.allOf(createTransaction.toCompletableFuture(), addTransaction.toCompletableFuture()).thenApply(x ->  Empty.getDefaultInstance());
//    return effects().asyncReply(result);

  }
}
