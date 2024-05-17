import { Button, Typography } from "@mui/material";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { useAccount } from "../hooks/useAccount";
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { yupResolver } from "@hookform/resolvers/yup";
import { LogInSchema } from "../validation/schemas";
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import FormComponent from "../components/FormComponent";
import TextFieldComponent from "../components/TextFieldComponent";
import { LoginCredentialsType } from "../types/Authentication";
import { useTranslation } from "react-i18next";

export default function LoginPage() {
  const {t} = useTranslation();
  const { logIn } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<LoginCredentialsType>({
    defaultValues: {
      username: "",
      password: "",
    },
    resolver: yupResolver(LogInSchema),
  });

  const onSubmit: SubmitHandler<LoginCredentialsType> = (data) => {
    logIn(data);
  };

  const onError: SubmitErrorHandler<LoginCredentialsType> = (errors) => {
    console.error(errors);
  };

  return (
    <ContainerWithPictureComponent>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
      >
        <Typography component="h1" variant="h3">
          {t("logInLink")}
        </Typography>
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
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{
            mt: 3,
            mb: 2,
          }}
        >
          {t("logInLink")}
        </Button>
        <Link to={Pathnames.public.forgotPassword}>
          {t("forgotPasswordLabel")}
        </Link>
        <Link to={Pathnames.public.signIn}>
          {t("signInLabel")}
        </Link>
      </FormComponent>
    </ContainerWithPictureComponent>
  );
}
