package com.ace.member.main.me.statement;


import com.ace.member.bean.Statement;

import java.util.List;

interface StatementContract {

	interface StatementView {
		void setBeginning(String currency, String beginning);

		void showStatementData(int nextPage, boolean canLoadMore);

		void showNextStatementData(int nextPage);
	}

	interface StatementPresenter {
		List<Statement> getStatementData();

		void getStatement(String dateStart, String dateEnd, String selectedCurrency, int page);
	}
}
