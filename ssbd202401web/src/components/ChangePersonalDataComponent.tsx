import { Box, Button, Divider, MenuItem, TextField, Typography } from "@mui/material";
import { Controller, SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { PersonalDataType } from "../types/PersonalData";
import { useAccount } from "../hooks/useAccount";
import { GenderEnum } from "../types/enums/Gender.enum";
import { useEffect } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { changePersonalDataSchema } from "../validation/schemas";

export default function ChangePersonalDataComponent() {
    const { account, updateMyPersonalData, getMyAccount } = useAccount();
    const { handleSubmit, control, formState: { errors }, trigger } = useForm<PersonalDataType>({
        defaultValues: {
            firstName: account ? account.firstName : "",
            lastName: account ? account.lastName : "",
            gender: account ? account.gender : 0
        },
        resolver: yupResolver(changePersonalDataSchema)
    });

    useEffect(() => {
        getMyAccount();
    }, []);

    const onSubmit: SubmitHandler<PersonalDataType> = async (data) => {
        updateMyPersonalData(data);
    }

    const onError: SubmitErrorHandler<PersonalDataType> = (error) => {
        console.error(error);
    }

    return (
        <Box 
            component={"form"}
            onSubmit={handleSubmit(onSubmit, onError)}
            margin={"3rem"}
            display={"flex"}
            flexDirection={"column"}
            alignItems={"left"}
        >
        <Typography variant="h4">Change personal data</Typography>
        <Divider 
            sx={{
                marginTop: "3rem",
                marginBottom: "3rem"
            }}
        ></Divider>
        <Controller
            name="firstName"
            control={control}
            render={({field}) => {
                return <TextField
                    value={field.value}
                    onChange={(e) => {
                        field.onChange(e);
                        setTimeout(() => trigger(e.target.name as keyof PersonalDataType), 500);
                    }}
                    id={field.name}
                    label="First name"
                    name={field.name}
                    error={errors.firstName ? true : false}
                >

                </TextField>
            }}
        >
        </Controller>
        <Typography
                color={"red"}
                fontSize={14}
                width={"inherit"}
                sx={{
                    marginBottom: "1rem"
                }}
            >
                {errors.firstName?.message}
            </Typography>
        <Controller
            name="lastName"
            control={control}
            render={({field}) => {
                return <TextField
                    value={field.value}
                    onChange={(e) => {
                        field.onChange(e);
                        setTimeout(() => trigger(e.target.name as keyof PersonalDataType), 500);
                    }}
                    id={field.name}
                    label="Last name"
                    name={field.name}
                    error={errors.firstName ? true : false}
                >
                </TextField>
            }}
        >
        </Controller>
        <Typography
                color={"red"}
                fontSize={14}
                width={"inherit"}
                sx={{
                    marginBottom: "1rem"
                }}
            >
                {errors.lastName?.message}
            </Typography>
             <Controller
                name="gender"
                control={control}
                render={({field}) => {
                    return <TextField
                        select
                        label="Gender"
                        value={field.value}
                        onChange={(e) => {
                            field.onChange(e);
                            setTimeout(() => trigger(e.target.name as keyof PersonalDataType), 500);
                        }}
                        id={field.name}
                        sx={{ marginTop: "1rem" }}
                        name={field.name}
                        autoComplete=""
                    >
                        {Object.keys(GenderEnum).map((key, value) => {
                            return <MenuItem key={key} value={value}>
                                {GenderEnum[key as keyof typeof GenderEnum].info}
                            </MenuItem>
                        })}
                    </TextField>
                }}
            >
            </Controller>
            <Typography
                color={"red"}
                fontSize={14}
                width={"inherit"}
                sx={{
                    marginBottom: "1rem"
                }}
            >
                {errors.gender?.message}
            </Typography>
            <Button
              type="submit"
              variant="contained"
              sx={{
                mt: 3,
                mb: 2,
                width: "fit-content",
                alignSelf: "center"
              }}
            >
              Save changes
            </Button>
        </Box>
    )
}