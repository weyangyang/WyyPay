package com.wyy.net.factory;

import com.wyy.net.interf.FeedbackInterf;
import com.wyy.net.interf.GetUpgradeInterf;
import com.wyy.net.interf.impl.FeedbackImpl;
import com.wyy.net.interf.impl.GetUpgradeImpl;

public class ExternalFactory extends AbstractFactory {
	private static ExternalFactory mExternalFactory;

	private ExternalFactory() {

	}

	public synchronized static ExternalFactory getInstance() {
		if (null == mExternalFactory) {
			mExternalFactory = new ExternalFactory();
		}
		return mExternalFactory;

	}



	@Override
	public FeedbackInterf createFeedback() {
		return new FeedbackImpl();
	}

	@Override
	public GetUpgradeInterf createGetUpgrade() {
		return new GetUpgradeImpl();
	}

	
}
