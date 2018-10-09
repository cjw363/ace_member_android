package com.ace.member.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.ace.member.R;
import com.ace.member.main.detail.bill_payment_detail.BillPaymentDetailActivity;
import com.ace.member.main.detail.deposit_detail.DepositDetailActivity;
import com.ace.member.main.detail.deposit_via_agent_detail.DepositViaAgentDetailActivity;
import com.ace.member.main.detail.exchange_detail.ExchangeDetailActivity;
import com.ace.member.main.detail.fee_detail.FeeDetailActivity;
import com.ace.member.main.detail.transfer_detail.TransferDetailActivity;
import com.ace.member.main.detail.withdraw_detail.WithdrawDetailActivity;
import com.ace.member.main.detail.withdraw_via_agent_detail.WithdrawViaAgentDetailActivity;
import com.ace.member.main.home.receive_to_acct.detail.R2ADetailActivity;
import com.ace.member.main.home.salary_loan.salary_loan_detail.SalaryLoanDetailActivity;
import com.ace.member.main.home.top_up.order_detail.TopUpOrderDetailActivity;
import com.ace.member.main.third_party.bill_payment.history_detail.BillPaymentHistoryDetailActivity;
import com.ace.member.main.third_party.edc.detail.EdcHistoryDetailActivity;
import com.ace.member.main.third_party.wsa.detail.WsaHistoryDetailActivity;
import com.og.utils.FileUtils;

public class TransactionType {
	/**
	 * 1) N/A
	 * 2) 202 Deposit (via Bank) 203 Withdraw (via Bank) 204 Deposit (via Agent) 205 Withdraw (via Agent) 206 Deposit (via Branch) 207 Withdraw (via Branch) 215 Withdraw Fee 217 Bank Fee 221 Transfer Out 222 Transfer In 223 Transfer Out (A2C) 224 Receive to Account 225 Transfer Fee 232 ACE Loan 233 ACE Return Loan 236 Loan (Partner) 237 Return Loan (Partner) 239 Loan Service Charge (Partner) 240 Salary Loan 241 Salary Loan Return 243 Salary Loan Service Charge
	 * 280 Compensation 290 Currency Exchange
	 * 4) 401 Phone Top Up
	 * 5) 501 Scratch Card 502 Scratch Card Prize 503 Lottery 504 Lottery Prize
	 * 6) 603 Pay WSA 605 Pay WSA Fee 607 Pay EDC 609 Pay EDC Fee 611 Pay Partner Bill 613 Pay Partner Bill Fee
	 */

	private static final int TRANSACTION_SUB_TYPE_202_DEPOSIT_VIA_BANK = 202;
	public static final int TRANSACTION_SUB_TYPE_203_WITHDRAW_VIA_BANK = 203;
	private static final int TRANSACTION_SUB_TYPE_204_DEPOSIT_VIA_AGENT = 204;
	public static final int TRANSACTION_SUB_TYPE_205_WITHDRAW_VIA_AGENT = 205;
	private static final int TRANSACTION_SUB_TYPE_206_DEPOSIT_VIA_BRANCH = 206;
	private static final int TRANSACTION_SUB_TYPE_207_WITHDRAW_VIA_BRANCH = 207;
	public static final int TRANSACTION_SUB_TYPE_215_WITHDRAW_FEE = 215;
	public static final int TRANSACTION_SUB_TYPE_217_BANK_FEE = 217;
	public static final int TRANSACTION_SUB_TYPE_221_TRANSFER_OUT = 221;
	public static final int TRANSACTION_SUB_TYPE_222_TRANSFER_IN = 222;
	public static final int TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C = 223;
	private static final int TRANSACTION_SUB_TYPE_224_RECEIVE_TO_ACCOUNT = 224;
	public static final int TRANSACTION_SUB_TYPE_225_TRANSFER_FEE = 225;
	private static final int TRANSACTION_SUB_TYPE_232_ACE_LOAN = 232;
	private static final int TRANSACTION_SUB_TYPE_233_ACE_RETURN_LOAN = 233;
	private static final int TRANSACTION_SUB_TYPE_236_PARTNER_LOAN = 236;
	private static final int TRANSACTION_SUB_TYPE_237_RETURN_PARTNER_LOAN = 237;
	private static final int TRANSACTION_SUB_TYPE_239_PARTNER_LOAN_SERVICE_CHARGE = 239;
	public static final int TRANSACTION_SUB_TYPE_240_SALARY_LOAN = 240;
	public static final int TRANSACTION_SUB_TYPE_241_RETURN_SALARY_LOAN = 241;
	private static final int TRANSACTION_SUB_TYPE_243_SALARY_LOAN_SERVICE_CHARGE = 243;
	private static final int TRANSACTION_SUB_TYPE_280_COMPENSATION = 280;
	private static final int TRANSACTION_SUB_TYPE_290_CURRENCY_EXCHANGE = 290;
	private static final int TRANSACTION_SUB_TYPE_401_PHONE_TOP_UP = 401;
	private static final int TRANSACTION_SUB_TYPE_501_SCRATCH_CARD = 501;
	private static final int TRANSACTION_SUB_TYPE_502_SCRATCH_CARD_PRIZE = 502;
	private static final int TRANSACTION_SUB_TYPE_503_LOTTERY = 503;
	private static final int TRANSACTION_SUB_TYPE_504_LOTTERY_PRIZE = 504;
	private static final int TRANSACTION_SUB_TYPE_603_PAY_WSA = 603;
	private static final int TRANSACTION_SUB_TYPE_605_PAY_WSA_FEE = 605;
	private static final int TRANSACTION_SUB_TYPE_607_PAY_EDC = 607;
	private static final int TRANSACTION_SUB_TYPE_609_PAY_EDC_FEE = 609;
	public static final int TRANSACTION_SUB_TYPE_611_PAY_PARTNER_BILL = 611;
	private static final int TRANSACTION_SUB_TYPE_613_PAY_PARTNER_BILL_FEE = 613;
	private static final int TRANSACTION_SUB_TYPE_614_PARTNER_BILL_RETURN = 614;
	private static final int TRANSACTION_SUB_TYPE_621_API_PAY = 621;

	private final static SparseIntArray mSubTypeNameArray = new SparseIntArray() {
		{
			put(TRANSACTION_SUB_TYPE_202_DEPOSIT_VIA_BANK, R.string.deposit_via_bank);
			put(TRANSACTION_SUB_TYPE_203_WITHDRAW_VIA_BANK, R.string.withdraw_via_bank);
			put(TRANSACTION_SUB_TYPE_204_DEPOSIT_VIA_AGENT, R.string.deposit_via_agent);
			put(TRANSACTION_SUB_TYPE_205_WITHDRAW_VIA_AGENT, R.string.withdraw_via_agent);
			put(TRANSACTION_SUB_TYPE_206_DEPOSIT_VIA_BRANCH, R.string.deposit_via_branch);
			put(TRANSACTION_SUB_TYPE_207_WITHDRAW_VIA_BRANCH, R.string.withdraw_via_branch);
			put(TRANSACTION_SUB_TYPE_215_WITHDRAW_FEE, R.string.withdraw_fee);
			put(TRANSACTION_SUB_TYPE_217_BANK_FEE, R.string.bank_fee);
			put(TRANSACTION_SUB_TYPE_221_TRANSFER_OUT, R.string.transfer_out);
			put(TRANSACTION_SUB_TYPE_222_TRANSFER_IN, R.string.transfer_in);
			put(TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C, R.string.transfer_out_a2c);
			put(TRANSACTION_SUB_TYPE_224_RECEIVE_TO_ACCOUNT, R.string.receive_to_account);
			put(TRANSACTION_SUB_TYPE_225_TRANSFER_FEE, R.string.transfer_fee);
			put(TRANSACTION_SUB_TYPE_232_ACE_LOAN, R.string.loan);
			put(TRANSACTION_SUB_TYPE_233_ACE_RETURN_LOAN, R.string.return_loan);
			put(TRANSACTION_SUB_TYPE_236_PARTNER_LOAN, R.string.loan_partner);
			put(TRANSACTION_SUB_TYPE_237_RETURN_PARTNER_LOAN, R.string.return_loan_partner);
			put(TRANSACTION_SUB_TYPE_239_PARTNER_LOAN_SERVICE_CHARGE, R.string.loan_service_charge_partner);
			put(TRANSACTION_SUB_TYPE_240_SALARY_LOAN, R.string.salary_loan);
			put(TRANSACTION_SUB_TYPE_241_RETURN_SALARY_LOAN, R.string.salary_loan_return);
			put(TRANSACTION_SUB_TYPE_243_SALARY_LOAN_SERVICE_CHARGE, R.string.salary_loan_service_charge);
			put(TRANSACTION_SUB_TYPE_280_COMPENSATION, R.string.compensation);
			put(TRANSACTION_SUB_TYPE_290_CURRENCY_EXCHANGE, R.string.currency_exchange);
			put(TRANSACTION_SUB_TYPE_401_PHONE_TOP_UP, R.string.phone_top_up);
			put(TRANSACTION_SUB_TYPE_501_SCRATCH_CARD, R.string.scratch_card);
			put(TRANSACTION_SUB_TYPE_502_SCRATCH_CARD_PRIZE, R.string.scratch_card_prize);
			put(TRANSACTION_SUB_TYPE_503_LOTTERY, R.string.lottery);
			put(TRANSACTION_SUB_TYPE_504_LOTTERY_PRIZE, R.string.lottery_prize);
			put(TRANSACTION_SUB_TYPE_603_PAY_WSA, R.string.pay_wsa);
			put(TRANSACTION_SUB_TYPE_605_PAY_WSA_FEE, R.string.pay_wsa_fee);
			put(TRANSACTION_SUB_TYPE_607_PAY_EDC, R.string.pay_edc);
			put(TRANSACTION_SUB_TYPE_609_PAY_EDC_FEE, R.string.pay_edc_fee);
			put(TRANSACTION_SUB_TYPE_611_PAY_PARTNER_BILL, R.string.pay_partner_bill);
			put(TRANSACTION_SUB_TYPE_613_PAY_PARTNER_BILL_FEE, R.string.pay_partner_bill_fee);
			put(TRANSACTION_SUB_TYPE_614_PARTNER_BILL_RETURN, R.string.partner_bill_return);
			put(TRANSACTION_SUB_TYPE_621_API_PAY, R.string.api_pay);
		}
	};

	private final static SparseIntArray mSubTypeIconArray = new SparseIntArray() {
		{
			put(TRANSACTION_SUB_TYPE_202_DEPOSIT_VIA_BANK, R.drawable.ic_deposit_via_bank);
			put(TRANSACTION_SUB_TYPE_203_WITHDRAW_VIA_BANK, R.drawable.ic_withdraw_via_bank);
			put(TRANSACTION_SUB_TYPE_204_DEPOSIT_VIA_AGENT, R.drawable.ic_deposit_via_agent);
			put(TRANSACTION_SUB_TYPE_205_WITHDRAW_VIA_AGENT, R.drawable.ic_withdraw_via_agent);
			put(TRANSACTION_SUB_TYPE_206_DEPOSIT_VIA_BRANCH, R.drawable.ic_deposit_via_branch);
			put(TRANSACTION_SUB_TYPE_207_WITHDRAW_VIA_BRANCH, R.drawable.ic_withdraw_via_branch);
			put(TRANSACTION_SUB_TYPE_215_WITHDRAW_FEE, R.drawable.ic_withdraw_fee);
			put(TRANSACTION_SUB_TYPE_217_BANK_FEE, R.drawable.ic_bank_fee);
			put(TRANSACTION_SUB_TYPE_221_TRANSFER_OUT, R.drawable.ic_transfer_out);
			put(TRANSACTION_SUB_TYPE_222_TRANSFER_IN, R.drawable.ic_transfer_in);
			put(TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C, R.drawable.ic_transfer_out_a2c);
			put(TRANSACTION_SUB_TYPE_224_RECEIVE_TO_ACCOUNT, R.drawable.ic_a2c);
			put(TRANSACTION_SUB_TYPE_225_TRANSFER_FEE, R.drawable.ic_transfer_fee);
			put(TRANSACTION_SUB_TYPE_232_ACE_LOAN, R.drawable.ic_loan);
			put(TRANSACTION_SUB_TYPE_233_ACE_RETURN_LOAN, R.drawable.ic_return_loan);
			put(TRANSACTION_SUB_TYPE_236_PARTNER_LOAN, R.drawable.ic_partner_loan);
			put(TRANSACTION_SUB_TYPE_237_RETURN_PARTNER_LOAN, R.drawable.ic_return_partner_loan);
			put(TRANSACTION_SUB_TYPE_239_PARTNER_LOAN_SERVICE_CHARGE, R.drawable.ic_partner_loan_service_charge);
			put(TRANSACTION_SUB_TYPE_240_SALARY_LOAN, R.drawable.ic_salary_loan);
			put(TRANSACTION_SUB_TYPE_241_RETURN_SALARY_LOAN, R.drawable.ic_return_salary_loan);
			put(TRANSACTION_SUB_TYPE_243_SALARY_LOAN_SERVICE_CHARGE, R.drawable.ic_salary_loan_service_charge);
			put(TRANSACTION_SUB_TYPE_280_COMPENSATION, R.drawable.ic_compensation);
			put(TRANSACTION_SUB_TYPE_290_CURRENCY_EXCHANGE, R.drawable.ic_exchange);
			put(TRANSACTION_SUB_TYPE_401_PHONE_TOP_UP, R.drawable.ic_top_up);
			put(TRANSACTION_SUB_TYPE_501_SCRATCH_CARD, R.drawable.ic_scratch);
			put(TRANSACTION_SUB_TYPE_502_SCRATCH_CARD_PRIZE, R.drawable.ic_scratch_prize);
			put(TRANSACTION_SUB_TYPE_503_LOTTERY, R.drawable.ic_lottery);
			put(TRANSACTION_SUB_TYPE_504_LOTTERY_PRIZE, R.drawable.ic_lottery_prize);
			put(TRANSACTION_SUB_TYPE_603_PAY_WSA, R.drawable.ic_pay_wsa);
			put(TRANSACTION_SUB_TYPE_605_PAY_WSA_FEE, R.drawable.ic_pay_wsa_fee);
			put(TRANSACTION_SUB_TYPE_607_PAY_EDC, R.drawable.ic_pay_edc);
			put(TRANSACTION_SUB_TYPE_609_PAY_EDC_FEE, R.drawable.ic_pay_edc_fee);
			put(TRANSACTION_SUB_TYPE_611_PAY_PARTNER_BILL, R.drawable.ic_pay_partner_bill);
			put(TRANSACTION_SUB_TYPE_613_PAY_PARTNER_BILL_FEE, R.drawable.ic_pay_partner_bill_fee);
			put(TRANSACTION_SUB_TYPE_614_PARTNER_BILL_RETURN, R.drawable.ic_balance);//待更换
			put(TRANSACTION_SUB_TYPE_621_API_PAY, R.drawable.ic_balance);//待更换
		}
	};

	private final static SparseArray<Class> mSubTypeDetailClassArray = new SparseArray<Class>() {
		{
			put(TRANSACTION_SUB_TYPE_202_DEPOSIT_VIA_BANK, DepositDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_203_WITHDRAW_VIA_BANK, WithdrawDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_204_DEPOSIT_VIA_AGENT, DepositViaAgentDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_205_WITHDRAW_VIA_AGENT, WithdrawViaAgentDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_215_WITHDRAW_FEE, FeeDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_217_BANK_FEE, FeeDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_221_TRANSFER_OUT, TransferDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_222_TRANSFER_IN, TransferDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C, TransferDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_225_TRANSFER_FEE, TransferDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_224_RECEIVE_TO_ACCOUNT, R2ADetailActivity.class);
			put(TRANSACTION_SUB_TYPE_240_SALARY_LOAN, SalaryLoanDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_241_RETURN_SALARY_LOAN, SalaryLoanDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_290_CURRENCY_EXCHANGE, ExchangeDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_401_PHONE_TOP_UP, TopUpOrderDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_603_PAY_WSA, WsaHistoryDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_605_PAY_WSA_FEE, WsaHistoryDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_607_PAY_EDC, EdcHistoryDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_609_PAY_EDC_FEE, EdcHistoryDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_611_PAY_PARTNER_BILL, BillPaymentDetailActivity.class);
			put(TRANSACTION_SUB_TYPE_613_PAY_PARTNER_BILL_FEE, BillPaymentDetailActivity.class);
		}
	};

	public static String getSubTypeName(Context context, int code) {
		String str = String.valueOf(code);
		try {
			int id = TransactionType.mSubTypeNameArray.get(code);
			if (id > 0) {
				str = context.getResources().getString(id);
			}
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return str;
	}

	public static int getSubTypeIcon(int code) {
		int icon = R.drawable.ic_balance;
		try {
			int id = TransactionType.mSubTypeIconArray.get(code);
			if (id > 0) {
				icon = id;
			}
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return icon;
	}

	public static Class getDetailActivityClass(int code) throws ClassNotFoundException {
		String className = TransactionType.mSubTypeDetailClassArray.get(code).getCanonicalName();
		return Class.forName(className);
	}

}
