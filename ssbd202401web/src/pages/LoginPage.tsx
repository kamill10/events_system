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

export default function LoginPage() {
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
    alert(errors);
  };

  return (
    <ContainerWithPictureComponent>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
      >
        <Typography component="h1" variant="h3">
          Log in
        </Typography>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="username"
          trigger={trigger}
          type="text"
          label="Username"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="password"
          trigger={trigger}
          type="password"
          label="Password"
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
          Log in
        </Button>
        <Link to={Pathnames.public.forgotPassword}>
          I forgot my password :(
        </Link>
        <Link to={Pathnames.public.signIn}>
          You don't have an account yet? Sign in!
        </Link>
      </FormComponent>
    </ContainerWithPictureComponent>
  );
}
