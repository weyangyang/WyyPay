package com.xuetangx.net.factory;

import com.xuetangx.net.interf.FeedbackInterf;
import com.xuetangx.net.interf.GetUpgradeInterf;
import com.xuetangx.net.interf.impl.FeedbackImpl;
import com.xuetangx.net.interf.impl.GetUpgradeImpl;

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
