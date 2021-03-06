package com.eventyay.organizer.core.organizer.password;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventyay.organizer.R;
import com.eventyay.organizer.common.mvp.view.BaseFragment;
import com.eventyay.organizer.databinding.ChangePasswordFragmentBinding;
import com.eventyay.organizer.ui.ViewUtils;

import javax.inject.Inject;

import br.com.ilhasoft.support.validation.Validator;
import dagger.Lazy;

import static com.eventyay.organizer.ui.ViewUtils.showView;

public class ChangePasswordFragment extends BaseFragment<ChangePasswordPresenter> implements ChangePasswordView {

    @Inject
    Lazy<ChangePasswordPresenter> presenterProvider;

    private ChangePasswordFragmentBinding binding;
    private Validator validator;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.change_password_fragment, container, false);
        validator = new Validator(binding);

        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().attach(this);
        binding.setOrganizerPassword(getPresenter().getChangePasswordObject());
        getPresenter().start();

        binding.btnChangePassword.setOnClickListener(view -> {
            if (!validator.validate())
                return;

            String url = binding.url.baseUrl.getText().toString().trim();
            getPresenter().setBaseUrl(url, binding.url.overrideUrl.isChecked());
            getPresenter().changePasswordRequest(binding.oldPassword.getText().toString(),
                binding.newPassword.getText().toString(),
                binding.confirmNewPassword.getText().toString());

        });
    }

    @Override
    protected int getTitle() {
        return R.string.change_password;
    }

    @Override
    public void showError(String error) {
        ViewUtils.hideKeyboard(binding.getRoot());
        ViewUtils.showSnackbar(binding.getRoot(), error);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showProgress(boolean show) {
        showView(binding.progressBar, show);
    }

    @Override
    public Lazy<ChangePasswordPresenter> getPresenterProvider() {
        return presenterProvider;
    }

}
