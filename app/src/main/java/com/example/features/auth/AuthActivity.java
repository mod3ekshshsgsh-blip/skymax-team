package com.example.features.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AICreatorApp;
import com.example.MainActivity;
import com.example.R;
import com.example.data.models.User;
import com.example.data.repositories.AuthRepository;

public class AuthActivity extends AppCompatActivity {
    private AuthRepository authRepository;

    private Button btnTabLogin;
    private Button btnTabRegister;
    private LinearLayout layoutLoginForm;
    private LinearLayout layoutRegisterForm;
    private TextView tvAuthSubtitle;
    private TextView tvAuthError;
    private ProgressBar progressAuth;

    // Login inputs
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnSubmitLogin;
    private TextView btnForgotPassword;

    // Register inputs
    private EditText etRegisterName;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterConfirmPassword;
    private Button btnSubmitRegister;

    // Alternative auth
    private Button btnGoogleSignIn;
    private Button btnGuestExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authRepository = AICreatorApp.getInstance().getAuthRepository();

        // If already logged in, navigate straight to MainActivity
        if (authRepository.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_auth);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnTabLogin = findViewById(R.id.btn_tab_login);
        btnTabRegister = findViewById(R.id.btn_tab_register);
        layoutLoginForm = findViewById(R.id.layout_login_form);
        layoutRegisterForm = findViewById(R.id.layout_register_form);
        tvAuthSubtitle = findViewById(R.id.tv_auth_subtitle);
        tvAuthError = findViewById(R.id.tv_auth_error);
        progressAuth = findViewById(R.id.progress_auth);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnSubmitLogin = findViewById(R.id.btn_submit_login);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);

        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = findViewById(R.id.et_register_confirm_password);
        btnSubmitRegister = findViewById(R.id.btn_submit_register);

        btnGoogleSignIn = findViewById(R.id.btn_google_signin);
        btnGuestExplore = findViewById(R.id.btn_guest_explore);
    }

    private void setupListeners() {
        btnTabLogin.setOnClickListener(v -> showLoginForm());
        btnTabRegister.setOnClickListener(v -> showRegisterForm());

        btnSubmitLogin.setOnClickListener(v -> handleLogin());
        btnSubmitRegister.setOnClickListener(v -> handleRegister());

        btnForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

        btnGoogleSignIn.setOnClickListener(v -> {
            setLoading(true);
            authRepository.login("creator.google@gmail.com", "google_secure_123", new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    setLoading(false);
                    Toast.makeText(AuthActivity.this, "Signed in with Google as " + user.getName(), Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                }

                @Override
                public void onError(String message) {
                    setLoading(false);
                    showError(message);
                }
            });
        });

        btnGuestExplore.setOnClickListener(v -> {
            setLoading(true);
            authRepository.guestLogin(new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    setLoading(false);
                    Toast.makeText(AuthActivity.this, "Welcome to AI Creator Studio!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                }

                @Override
                public void onError(String message) {
                    setLoading(false);
                    showError(message);
                }
            });
        });
    }

    private void showLoginForm() {
        hideError();
        layoutLoginForm.setVisibility(View.VISIBLE);
        layoutRegisterForm.setVisibility(View.GONE);
        tvAuthSubtitle.setText("Create high-converting AI content with studio precision");

        btnTabLogin.setBackgroundResource(R.drawable.bg_button_primary);
        btnTabLogin.setTextColor(getColor(R.color.text_white));
        btnTabRegister.setBackgroundColor(getColor(R.color.transparent));
        btnTabRegister.setTextColor(getColor(R.color.on_surface_variant));
    }

    private void showRegisterForm() {
        hideError();
        layoutLoginForm.setVisibility(View.GONE);
        layoutRegisterForm.setVisibility(View.VISIBLE);
        tvAuthSubtitle.setText("Join 50,000+ creators producing viral content daily");

        btnTabRegister.setBackgroundResource(R.drawable.bg_button_primary);
        btnTabRegister.setTextColor(getColor(R.color.text_white));
        btnTabLogin.setBackgroundColor(getColor(R.color.transparent));
        btnTabLogin.setTextColor(getColor(R.color.on_surface_variant));
    }

    private void handleLogin() {
        hideError();
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            showError("Please enter your email address");
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        setLoading(true);
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoading(false);
                Toast.makeText(AuthActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }

            @Override
            public void onError(String message) {
                setLoading(false);
                showError(message);
            }
        });
    }

    private void handleRegister() {
        hideError();
        String name = etRegisterName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String confirmPassword = etRegisterConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            showError("Please enter your full name");
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            showError("Please enter a valid email address");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        setLoading(true);
        authRepository.register(name, email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoading(false);
                Toast.makeText(AuthActivity.this, "Account created! Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }

            @Override
            public void onError(String message) {
                setLoading(false);
                showError(message);
            }
        });
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText etResetEmail = dialogView.findViewById(R.id.et_reset_email);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_reset);
        Button btnSend = dialogView.findViewById(R.id.btn_send_reset);

        String currentEmail = etLoginEmail.getText().toString().trim();
        if (!currentEmail.isEmpty()) {
            etResetEmail.setText(currentEmail);
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSend.setOnClickListener(v -> {
            String email = etResetEmail.getText().toString().trim();
            if (email.isEmpty() || !email.contains("@")) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }
            authRepository.sendPasswordReset(email, new AuthRepository.SimpleCallback() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    Toast.makeText(AuthActivity.this, "Password reset instructions sent to " + email, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        tvAuthError.setText(message);
        tvAuthError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvAuthError.setVisibility(View.GONE);
    }

    private void setLoading(boolean loading) {
        progressAuth.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSubmitLogin.setEnabled(!loading);
        btnSubmitRegister.setEnabled(!loading);
        btnGoogleSignIn.setEnabled(!loading);
        btnGuestExplore.setEnabled(!loading);
    }
}
