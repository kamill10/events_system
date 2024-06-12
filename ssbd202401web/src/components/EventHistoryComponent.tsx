import {useTranslation} from "react-i18next";
import {useEvents} from "../hooks/useEvents.ts";
import {EventWithNumberOfElements, PaginationPastEventResponse} from "../types/Event.ts";
import {useEffect, useState} from "react";
import {Controller, SubmitErrorHandler, SubmitHandler, useForm} from "react-hook-form";
import {PaginationRequestParams} from "../types/PaginationRequestParams.ts";
import {yupResolver} from "@hookform/resolvers/yup";
import {PaginationRequestParamsSchema} from "../validation/schemas.ts";
import {isInstanceOf} from "../utils";
import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Box,
    Button,
    MenuItem, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination,
    TextField, ToggleButton, ToggleButtonGroup,
    Typography
} from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import FormComponent from "./FormComponent.tsx";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import {HistoricalEventRowComponent} from "./HistoricalEventRowComponent.tsx";

export function  EventHistoryComponent  ()  {
const {t} = useTranslation ();
const {getMyPastEvents} = useEvents();
const [events, setEvents] = useState<EventWithNumberOfElements | null>();
const [open, setOpen] = useState(false);

const { handleSubmit, control, setValue, getValues } =
        useForm<PaginationRequestParams>({
            defaultValues: {
                page: 0,
                size: 5,
                direction: "asc",
                key: "startDate",
            },
            resolver: yupResolver(PaginationRequestParamsSchema),
        });

const getHistoricalEvents = async () => {
    const response = await getMyPastEvents(getValues());
    if (isInstanceOf<PaginationPastEventResponse>(response, "pageable")) {
        setEvents({
            numberOfElements: response.numberOfElements,
            events: response.content,
        });
    }
}

    const onSubmit: SubmitHandler<PaginationRequestParams> = async () => {
        await getHistoricalEvents();
        setOpen(false);
    };

    useEffect(() => {
        getHistoricalEvents();
    }, []);

    const onError: SubmitErrorHandler<PaginationRequestParams> = (error) => {
        console.error(error);
    };

    const handleChangePage = (_: any, newPage: number) => {
        setValue("page", newPage);
        handleSubmit(onSubmit)();
    };

    const handleChangeRowsPerPage = (event: any) => {
        const newRowsPerPage = parseInt(event.target.value, 10);
        setValue("page", 0);
        setValue("size", newRowsPerPage);
        handleSubmit(onSubmit)();
    };

    return (
        <Box
            sx={{
                marginTop: 4,
                marginLeft: 5,
            }}
        >
            <Typography variant="h4">{t("history")}</Typography>
            <Button
                onClick={getHistoricalEvents}
                variant="contained"
                startIcon={<RefreshIcon></RefreshIcon>}
                color="secondary"
                sx={{
                    mt: 1,
                    mb: 2,
                    width: "fit-content",
                    alignSelf: "center",
                }}
            >
                {t("refreshData")}
            </Button>
            <Accordion
                expanded={open}
                onChange={() => setOpen(!open)}
                sx={{
                    marginBottom: "1rem",
                }}
            >
                <AccordionSummary expandIcon={<ArrowDownwardIcon />}>
                    <Typography variant="h5">{t("sortData")}</Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ padding: "1px" }}>
                    <FormComponent
                        handleSubmit={handleSubmit}
                        onError={onError}
                        onSubmit={onSubmit}
                        align="start"
                    >
                        <Typography variant="h6">{t("sortDataKeyTitle")}</Typography>
                        <Typography variant="body2">{t("sortDataKeyBody")}</Typography>
                        <Controller
                            name="key"
                            control={control}
                            render={({ field }) => {
                                return (
                                    <TextField
                                        select
                                        label={t("filterKey")}
                                        fullWidth
                                        value={field.value}
                                        onChange={(e) => {
                                            field.onChange(e);
                                        }}
                                        id={field.name}
                                        sx={{ marginTop: "1rem" }}
                                        name={field.name}
                                        autoComplete=""
                                    >
                                        <MenuItem value="id">{t("id")}</MenuItem>
                                        <MenuItem value="name">{t("eventName")}</MenuItem>
                                        <MenuItem value="description">
                                            {t("description")}
                                        </MenuItem>
                                        <MenuItem value="startDate">
                                            {t("startDateLabel")}
                                        </MenuItem>
                                        <MenuItem value="endDate">
                                            {t("endDateLabel")}
                                        </MenuItem>
                                    </TextField>
                                );
                            }}
                        ></Controller>
                        <Typography variant="h6" marginTop={3}>
                            {t("sortDataDirTitle")}
                        </Typography>
                        <Typography variant="body2">{t("sortDataDirBody")}</Typography>
                        <Controller
                            name="direction"
                            control={control}
                            render={({ field }) => {
                                return (
                                    <ToggleButtonGroup
                                        value={field.value}
                                        exclusive
                                        sx={{
                                            marginTop: "1rem",
                                        }}
                                        onChange={(e) => {
                                            field.onChange(e);
                                        }}
                                        id={field.name}
                                    >
                                        <ToggleButton value="asc">{t("ascending")}</ToggleButton>
                                        <ToggleButton value="desc">{t("descending")}</ToggleButton>
                                    </ToggleButtonGroup>
                                );
                            }}
                        ></Controller>
                        <Button
                            type="submit"
                            variant="contained"
                            sx={{
                                marginY: 3,
                            }}
                            startIcon={<FilterAltIcon />}
                        >
                            {t("sortData")}
                        </Button>
                    </FormComponent>
                </AccordionDetails>
            </Accordion>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableCell
                            sx={{
                                fontWeight: "bold",
                                fontSize: "18px",
                            }}
                            align="left"
                        >
                            ID
                        </TableCell>
                        <TableCell
                            sx={{
                                fontWeight: "bold",
                                fontSize: "18px",
                            }}
                            align="right"
                        >
                            {t("eventName")}
                        </TableCell>
                        <TableCell
                            sx={{
                                fontWeight: "bold",
                                fontSize: "18px",
                            }}
                            align="right"
                        >
                            {t("description")}
                        </TableCell>
                        <TableCell
                            sx={{
                                fontWeight: "bold",
                                fontSize: "18px",
                            }}
                            align="right"
                        >
                            {t("startDateLabel")}
                        </TableCell>
                        <TableCell
                            sx={{
                                fontWeight: "bold",
                                fontSize: "18px",
                            }}
                            align="right"
                        >
                            {t("endDateLabel")}
                        </TableCell>
                    </TableHead>
                    <TableBody>
                                {events?.events.map((event) => (
                                <HistoricalEventRowComponent key={event.id} event={event} />
                            ))}
                    </TableBody>
                </Table>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    component="div"
                    count={events?.numberOfElements ?? 0}
                    rowsPerPage={getValues().size ?? 5}
                    page={getValues().page ?? 0}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    labelRowsPerPage={t("rowsPerPage")}
                    labelDisplayedRows={({ from, to, count }) => {
                        return `${t("displayedRows")} ${from}-${to} ${t("of")} ${count}`;
                    }}
                />
            </TableContainer>
        </Box>
    );

}