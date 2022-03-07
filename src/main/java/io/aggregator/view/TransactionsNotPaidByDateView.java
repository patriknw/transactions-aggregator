package io.aggregator.view;

import com.akkaserverless.javasdk.view.View;
import com.akkaserverless.javasdk.view.ViewContext;
import com.google.protobuf.Any;
import io.aggregator.entity.TransactionEntity;

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
// This is the implementation for the View Service described in your io/aggregator/view/transactions_not_paid_by_date.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class TransactionsNotPaidByDateView extends AbstractTransactionsNotPaidByDateView {

  public TransactionsNotPaidByDateView(ViewContext context) {
  }

  @Override
  public TransactionModel.Transaction emptyState() {
    return TransactionModel.Transaction.getDefaultInstance();
  }

  @Override
  public View.UpdateEffect<TransactionModel.Transaction> onTransactionCreated(TransactionModel.Transaction state, TransactionEntity.TransactionCreated event) {
    return effects().updateState(TransactionsEventHandler.handle(state, event));
  }

  @Override
  public View.UpdateEffect<TransactionModel.Transaction> onPaymentAdded(TransactionModel.Transaction state, TransactionEntity.PaymentAdded event) {
    return effects().updateState(TransactionsEventHandler.handle(state, event));
  }

  @Override
  public View.UpdateEffect<TransactionModel.Transaction> ignoreOtherEvents(TransactionModel.Transaction state, Any any) {
    return effects().ignore();
  }
}
