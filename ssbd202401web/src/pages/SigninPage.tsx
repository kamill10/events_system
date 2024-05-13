import { Button, MenuItem, TextField, Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { AccountSingInType } from "../types/Account";
import { GenderEnum } from "../types/enums/Gender.enum";
import { signInValidationSchema } from "../validation/schemas";
import { yupResolver } from "@hookform/resolvers/yup";
import { Pathnames } from "../router/Pathnames";
import { Link } from "react-router-dom";
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import FormComponent from "../components/FormComponent";
import TextFieldComponent from "../components/TextFieldComponent";

export default function SigninPage() {
  const { signIn } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<AccountSingInType>({
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

  const onSubmit: SubmitHandler<AccountSingInType> = async (data) => {
    await signIn(data);
  };

  const onError: SubmitErrorHandler<AccountSingInType> = (errors) => {
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
          Sign in
        </Typography>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="firstName"
          trigger={trigger}
          type="text"
          label="First name"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="lastName"
          trigger={trigger}
          type="text"
          label="Last name"
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
        <TextFieldComponent
          control={control}
          errors={errors}
          name="confirmPassword"
          trigger={trigger}
          type="password"
          label="Confirm password"
        />
        <Controller
          name="gender"
          control={control}
          render={({ field }) => {
            return (
              <TextField
                select
                label="Gender"
                value={field.value}
                onChange={(e) => {
                  field.onChange(e);
                  setTimeout(
                    () => trigger(e.target.name as keyof AccountSingInType),
                    500,
                  );
                }}
                id={field.name}
                sx={{ marginTop: "1rem" }}
                name={field.name}
                autoComplete=""
              >
                {Object.keys(GenderEnum).map((key, value) => {
                  return (
                    <MenuItem key={key} value={value}>
                      {GenderEnum[key as keyof typeof GenderEnum].info}
                    </MenuItem>
                  );
                })}
              </TextField>
            );
          }}
        ></Controller>
        <Button
          type="submit"
          variant="contained"
          sx={{
            mt: 3,
            mb: 2,
          }}
        >
          Sign in
        </Button>
        <Link to={Pathnames.public.login}>
          Already have an account? Log in!
        </Link>
      </FormComponent>
    </ContainerWithPictureComponent>
  );
}
