package org.qortal.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qortal.api.resource.TransactionsResource.ConfirmationStatus;
import org.qortal.data.transaction.ArbitraryTransactionData;
import org.qortal.data.transaction.TransactionData;
import org.qortal.repository.DataException;
import org.qortal.repository.Repository;
import org.qortal.repository.RepositoryManager;
import org.qortal.transaction.ArbitraryTransaction;
import org.qortal.transaction.Transaction.TransactionType;

public class ArbitraryDataManager extends Thread {

	private static final Logger LOGGER = LogManager.getLogger(ArbitraryDataManager.class);
	private static final List<TransactionType> ARBITRARY_TX_TYPE = Arrays.asList(TransactionType.ARBITRARY);

	private static ArbitraryDataManager instance;

	private volatile boolean isStopping = false;

	private ArbitraryDataManager() {
	}

	public static ArbitraryDataManager getInstance() {
		if (instance == null)
			instance = new ArbitraryDataManager();

		return instance;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Arbitrary Data Manager");

		try {
			while (!isStopping) {
				Thread.sleep(2000);

				// Any arbitrary transactions we want to fetch data for?
				try (final Repository repository = RepositoryManager.getRepository()) {
					List<byte[]> signatures = repository.getTransactionRepository().getSignaturesMatchingCriteria(null, null, null, ARBITRARY_TX_TYPE, null, null, ConfirmationStatus.BOTH, null, null, true);
					if (signatures == null || signatures.isEmpty()) {
						continue;
					}

					// Filter out those that already have local data
					signatures.removeIf(signature -> hasLocalData(repository, signature));

					if (signatures.isEmpty()) {
						continue;
					}

					// Pick one at random
					final int index = new Random().nextInt(signatures.size());
					byte[] signature = signatures.get(index);

					// Ask our connected peers if they have files for this signature
					// This process automatically then fetches the files themselves if a peer is found
					Controller.getInstance().fetchArbitraryDataFileList(signature);

				} catch (DataException e) {
					LOGGER.error("Repository issue when fetching arbitrary transaction data", e);
				}
			}
		} catch (InterruptedException e) {
			// Fall-through to exit thread...
		}
	}

	public void shutdown() {
		isStopping = true;
		this.interrupt();
	}

	private boolean hasLocalData(final Repository repository, final byte[] signature) {
		try {
			TransactionData transactionData = repository.getTransactionRepository().fromSignature(signature);
			if (!(transactionData instanceof ArbitraryTransactionData))
				return true;

			ArbitraryTransaction arbitraryTransaction = new ArbitraryTransaction(repository, transactionData);

			return arbitraryTransaction.isDataLocal();
		} catch (DataException e) {
			LOGGER.error("Repository issue when checking arbitrary transaction's data is local", e);
			return true;
		}
	}

}
