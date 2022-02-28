package io.aggregator.entity;

import io.aggregator.TimeTo;
import io.aggregator.api.MinuteApi;

import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class MinuteTest {

  @Test
  public void exampleTest() {
    // MinuteTestKit testKit = MinuteTestKit.of(Minute::new);
    // use the testkit to execute a command
    // of events emitted, or a final updated state:
    // EventSourcedResult<SomeResponse> result = testKit.someOperation(SomeRequest);
    // verify the emitted events
    // ExpectedEvent actualEvent = result.getNextEventOfType(ExpectedEvent.class);
    // assertEquals(expectedEvent, actualEvent)
    // verify the final state after applying the events
    // assertEquals(expectedState, testKit.getState());
    // verify the response
    // SomeResponse actualResponse = result.getReply();
    // assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void addSecondTest() {
    MinuteTestKit testKit = MinuteTestKit.of(Minute::new);

    var epochSecond = TimeTo.fromNow().toEpochSecond();
    var nextEpochSecond = TimeTo.fromEpochSecond(epochSecond).plus().seconds(1).toEpochSecond();
    var epochMinute = TimeTo.fromEpochSecond(epochSecond).toEpochMinute();

    var response = testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(epochSecond)
            .build());

    var minuteCreated = response.getNextEventOfType(MinuteEntity.MinuteCreated.class);
    var secondAdded = response.getNextEventOfType(MinuteEntity.SecondAdded.class);

    assertEquals("merchant-1", minuteCreated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", minuteCreated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", minuteCreated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", minuteCreated.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, minuteCreated.getEpochMinute());

    assertEquals("merchant-1", secondAdded.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", secondAdded.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", secondAdded.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", secondAdded.getMerchantKey().getAccountTo());
    assertEquals(epochSecond, secondAdded.getEpochSecond());

    var state = testKit.getState();

    assertEquals("merchant-1", state.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", state.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", state.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", state.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, state.getEpochMinute());
    assertEquals(1, state.getActiveSecondsCount());

    var activeSecond = state.getActiveSeconds(0);

    assertEquals(epochSecond, activeSecond.getEpochSecond());

    response = testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(nextEpochSecond)
            .build());

    secondAdded = response.getNextEventOfType(MinuteEntity.SecondAdded.class);

    assertEquals("merchant-1", secondAdded.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", secondAdded.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", secondAdded.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", secondAdded.getMerchantKey().getAccountTo());
    assertEquals(nextEpochSecond, secondAdded.getEpochSecond());

    state = testKit.getState();

    assertEquals("merchant-1", state.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", state.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", state.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", state.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, state.getEpochMinute());
    assertEquals(2, state.getActiveSecondsCount());

    activeSecond = state.getActiveSeconds(1);

    assertEquals(nextEpochSecond, activeSecond.getEpochSecond());
  }

  @Test
  public void aggregateMinuteTest() {
    MinuteTestKit testKit = MinuteTestKit.of(Minute::new);

    var epochSecond = TimeTo.fromNow().toEpochSecond();
    var nextEpochSecond = TimeTo.fromEpochSecond(epochSecond).plus().seconds(1).toEpochSecond();
    var epochMinute = TimeTo.fromEpochSecond(epochSecond).toEpochMinute();
    var now = TimeTo.fromEpochSecond(epochSecond).toTimestamp();

    testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(epochSecond)
            .build());

    testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(nextEpochSecond)
            .build());

    var response = testKit.aggregateMinute(
        MinuteApi.AggregateMinuteCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setAggregateRequestTimestamp(now)
            .setPaymentId("payment-1")
            .build());

    var minuteAggregationRequested = response.getNextEventOfType(MinuteEntity.MinuteAggregationRequested.class);

    assertEquals("merchant-1", minuteAggregationRequested.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", minuteAggregationRequested.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", minuteAggregationRequested.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", minuteAggregationRequested.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, minuteAggregationRequested.getEpochMinute());
    assertEquals(now, minuteAggregationRequested.getAggregateRequestTimestamp());
    assertEquals(2, minuteAggregationRequested.getEpochSecondsCount());
    assertEquals(epochSecond, minuteAggregationRequested.getEpochSeconds(0));
    assertEquals(nextEpochSecond, minuteAggregationRequested.getEpochSeconds(1));
    assertEquals("payment-1", minuteAggregationRequested.getPaymentId());

    var state = testKit.getState();

    var aggregateMinute = state.getAggregateMinutesList().stream()
        .filter(aggMin -> aggMin.getAggregateRequestTimestamp().equals(now))
        .findFirst();
    assertTrue(aggregateMinute.isPresent());
    assertEquals(now, aggregateMinute.get().getAggregateRequestTimestamp());
  }

  @Test
  public void aggregateMinuteWithNoSecondsTest() {
    MinuteTestKit testKit = MinuteTestKit.of(Minute::new);

    var epochSecond = TimeTo.fromNow().toEpochSecond();
    var epochMinute = TimeTo.fromEpochSecond(epochSecond).toEpochMinute();
    var now = TimeTo.fromEpochSecond(epochSecond).toTimestamp();

    var response = testKit.aggregateMinute(
        MinuteApi.AggregateMinuteCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setAggregateRequestTimestamp(now)
            .setPaymentId("payment-1")
            .build());

    var minuteAggregated = response.getNextEventOfType(MinuteEntity.MinuteAggregated.class);

    assertEquals("merchant-1", minuteAggregated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", minuteAggregated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", minuteAggregated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", minuteAggregated.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, minuteAggregated.getEpochMinute());
    assertEquals(now, minuteAggregated.getAggregateRequestTimestamp());
    assertEquals(0, minuteAggregated.getTransactionCount());
    assertEquals(0.0, minuteAggregated.getTransactionTotalAmount(), 0.0);
  }

  @Test
  public void secondAggregationTest() {
    MinuteTestKit testKit = MinuteTestKit.of(Minute::new);

    var epochSecond = TimeTo.fromNow().toEpochSecond();
    var nextEpochSecond = TimeTo.fromEpochSecond(epochSecond).plus().seconds(1).toEpochSecond();
    var epochMinute = TimeTo.fromEpochSecond(epochSecond).toEpochMinute();
    var aggregateRequestTimestamp = TimeTo.fromEpochSecond(epochSecond).toTimestamp();

    testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(epochSecond)
            .build());

    testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(nextEpochSecond)
            .build());

    testKit.aggregateMinute(
        MinuteApi.AggregateMinuteCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setAggregateRequestTimestamp(aggregateRequestTimestamp)
            .setPaymentId("payment-1")
            .build());

    var response = testKit.secondAggregation(
        MinuteApi.SecondAggregationCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(epochSecond)
            .setTransactionTotalAmount(123.45)
            .setTransactionCount(10)
            .setLastUpdateTimestamp(aggregateRequestTimestamp)
            .setAggregateRequestTimestamp(aggregateRequestTimestamp)
            .setPaymentId("payment-1")
            .build());

    var activeSecondAggregated = response.getNextEventOfType(MinuteEntity.ActiveSecondAggregated.class);

    assertEquals("merchant-1", activeSecondAggregated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", activeSecondAggregated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", activeSecondAggregated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", activeSecondAggregated.getMerchantKey().getAccountTo());
    assertEquals(epochSecond, activeSecondAggregated.getEpochSecond());
    assertEquals(123.45, activeSecondAggregated.getTransactionTotalAmount(), 0.0);
    assertEquals(10, activeSecondAggregated.getTransactionCount());
    assertEquals(aggregateRequestTimestamp, activeSecondAggregated.getLastUpdateTimestamp());
    assertEquals(aggregateRequestTimestamp, activeSecondAggregated.getAggregateRequestTimestamp());
    assertEquals("payment-1", activeSecondAggregated.getPaymentId());

    response = testKit.secondAggregation(
        MinuteApi.SecondAggregationCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(nextEpochSecond)
            .setTransactionTotalAmount(678.90)
            .setTransactionCount(20)
            .setLastUpdateTimestamp(aggregateRequestTimestamp)
            .setAggregateRequestTimestamp(aggregateRequestTimestamp)
            .setPaymentId("payment-1")
            .build());

    var minuteAggregated = response.getNextEventOfType(MinuteEntity.MinuteAggregated.class);
    activeSecondAggregated = response.getNextEventOfType(MinuteEntity.ActiveSecondAggregated.class);

    assertEquals("merchant-1", activeSecondAggregated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", activeSecondAggregated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", activeSecondAggregated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", activeSecondAggregated.getMerchantKey().getAccountTo());
    assertEquals(nextEpochSecond, activeSecondAggregated.getEpochSecond());
    assertEquals(678.90, activeSecondAggregated.getTransactionTotalAmount(), 0.0);
    assertEquals(20, activeSecondAggregated.getTransactionCount());
    assertEquals(aggregateRequestTimestamp, activeSecondAggregated.getLastUpdateTimestamp());
    assertEquals(aggregateRequestTimestamp, activeSecondAggregated.getAggregateRequestTimestamp());
    assertEquals("payment-1", activeSecondAggregated.getPaymentId());

    assertEquals("merchant-1", minuteAggregated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", minuteAggregated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", minuteAggregated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", minuteAggregated.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, minuteAggregated.getEpochMinute());
    assertEquals(123.45 + 678.90, minuteAggregated.getTransactionTotalAmount(), 0.0);
    assertEquals(10 + 20, minuteAggregated.getTransactionCount());
    assertEquals(aggregateRequestTimestamp, minuteAggregated.getLastUpdateTimestamp());
    assertEquals(aggregateRequestTimestamp, minuteAggregated.getAggregateRequestTimestamp());
    assertEquals("payment-1", minuteAggregated.getPaymentId());

    // this sequence re-activates the second and minute aggregation
    aggregateRequestTimestamp = TimeTo.fromEpochSecond(epochSecond).plus().minutes(1).toTimestamp();

    testKit.addSecond(
        MinuteApi.AddSecondCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(epochSecond)
            .build());

    testKit.aggregateMinute(
        MinuteApi.AggregateMinuteCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setAggregateRequestTimestamp(aggregateRequestTimestamp)
            .setPaymentId("payment-1")
            .build());

    response = testKit.secondAggregation(
        MinuteApi.SecondAggregationCommand
            .newBuilder()
            .setMerchantId("merchant-1")
            .setServiceCode("service-code-1")
            .setAccountFrom("account-from-1")
            .setAccountTo("account-to-1")
            .setEpochMinute(epochMinute)
            .setEpochSecond(epochSecond)
            .setTransactionTotalAmount(543.21)
            .setTransactionCount(321)
            .setLastUpdateTimestamp(aggregateRequestTimestamp)
            .setAggregateRequestTimestamp(aggregateRequestTimestamp)
            .setPaymentId("payment-1")
            .build());

    minuteAggregated = response.getNextEventOfType(MinuteEntity.MinuteAggregated.class);
    activeSecondAggregated = response.getNextEventOfType(MinuteEntity.ActiveSecondAggregated.class);

    assertEquals("merchant-1", activeSecondAggregated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", activeSecondAggregated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", activeSecondAggregated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", activeSecondAggregated.getMerchantKey().getAccountTo());
    assertEquals(epochSecond, activeSecondAggregated.getEpochSecond());
    assertEquals(543.21, activeSecondAggregated.getTransactionTotalAmount(), 0.0);
    assertEquals(321, activeSecondAggregated.getTransactionCount());
    assertEquals(aggregateRequestTimestamp, activeSecondAggregated.getLastUpdateTimestamp());
    assertEquals(aggregateRequestTimestamp, activeSecondAggregated.getAggregateRequestTimestamp());
    assertEquals("payment-1", activeSecondAggregated.getPaymentId());

    assertEquals("merchant-1", minuteAggregated.getMerchantKey().getMerchantId());
    assertEquals("service-code-1", minuteAggregated.getMerchantKey().getServiceCode());
    assertEquals("account-from-1", minuteAggregated.getMerchantKey().getAccountFrom());
    assertEquals("account-to-1", minuteAggregated.getMerchantKey().getAccountTo());
    assertEquals(epochMinute, minuteAggregated.getEpochMinute());
    assertEquals(543.21, minuteAggregated.getTransactionTotalAmount(), 0.0);
    assertEquals(321, minuteAggregated.getTransactionCount());
    assertEquals(aggregateRequestTimestamp, minuteAggregated.getLastUpdateTimestamp());
    assertEquals(aggregateRequestTimestamp, minuteAggregated.getAggregateRequestTimestamp());
    assertEquals("payment-1", minuteAggregated.getPaymentId());
  }
}
