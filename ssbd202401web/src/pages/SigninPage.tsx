import { Button, Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { signInValidationSchema } from "../validation/schemas";
import { yupResolver } from "@hookform/resolvers/yup";
import { Pathnames } from "../router/Pathnames";
import { Link } from "react-router-dom";
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import FormComponent from "../components/FormComponent";
import TextFieldComponent from "../components/TextFieldComponent";
import { SignInCredentialsType } from "../types/Authentication";
import { useTranslation } from "react-i18next";
import GenderListComponent from "../components/GenderListComponent";

export default function SigninPage() {
  const { t } = useTranslation();
  const { signIn } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<SignInCredentialsType>({
    defaultValues: {
      username: "",
      password: "",
      email: "",
      gender: 0,
      firstName: "",
      lastName: "",
      language: navigator.language == "pl" ? "POLISH" : "ENGLISH",
      confirmPassword: "",
    },
    resolver: yupResolver(signInValidationSchema),
  });

  const onSubmit: SubmitHandler<SignInCredentialsType> = async (data) => {
    await signIn(data);
  };

  const onError: SubmitErrorHandler<SignInCredentialsType> = (errors) => {
    console.error(errors);
  };

  return (
    <ContainerWithPictureComponent>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
      >
        <Typography variant="h3" component={"h1"}>
          {t("signInHeading")}
        </Typography>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="firstName"
          trigger={trigger}
          type="text"
          label={t("firstName")}
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="lastName"
          trigger={trigger}
          type="text"
          label={t("lastName")}
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="email"
          trigger={trigger}
          type="text"
          label="E-mail"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="username"
          trigger={trigger}
          type="text"
          label={t("usernameLabel")}
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="password"
          trigger={trigger}
          type="password"
          label={t("passwordLabel")}
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="confirmPassword"
          trigger={trigger}
          type="password"
          label={t("confirmPasswordLabel")}
        />
        <GenderListComponent control={control} errors={errors} name="gender" />
        <Button
          type="submit"
          variant="contained"
          sx={{
            mt: 3,
            mb: 2,
          }}
        >
          {t("signInHeading")}
        </Button>
        <Link to={Pathnames.public.login}>{t("haveAccountLabel")}</Link>
      </FormComponent>
    </ContainerWithPictureComponent>
  );
}
