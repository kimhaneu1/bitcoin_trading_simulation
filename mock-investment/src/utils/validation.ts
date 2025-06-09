// src/utils/validation.ts

export const validationRules = {
  isValidEmail: (email: string) => /\S+@\S+\.\S+/.test(email),
  isValidPassword: (password: string) => password.length >= 8,
};

interface SignupFormData {
  email: string;
  password: string;
  passwordConfirm: string;
  username: string;
  phoneNumber: string;
  agreeTerms: boolean;
}

export const validateSignupForm = (formData: SignupFormData) => {
  const errors: { [key: string]: string } = {};

  if (!formData.email) {
    errors.email = '이메일을 입력해주세요.';
  } else if (!validationRules.isValidEmail(formData.email)) {
    errors.email = '유효한 이메일 형식이 아닙니다.';
  }

  if (!formData.password) {
    errors.password = '비밀번호를 입력해주세요.';
  } else if (!validationRules.isValidPassword(formData.password)) {
    errors.password = '비밀번호는 8자 이상이어야 합니다.';
  }

  if (formData.password !== formData.passwordConfirm) {
    errors.passwordConfirm = '비밀번호가 일치하지 않습니다.';
  }

  if (!formData.username) {
    errors.username = '이름을 입력해주세요.';
  }

  if (!formData.phoneNumber) {
    errors.phoneNumber = '전화번호를 입력해주세요.';
  } else if (!/^010-\d{4}-\d{4}$/.test(formData.phoneNumber)) {
    errors.phoneNumber = '전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678';
  }

  if (!formData.agreeTerms) {
    errors.terms = '이용약관 및 개인정보처리방침에 동의해주세요.';
  }

  return errors;
};
