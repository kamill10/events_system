import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Button,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TextField,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import RoomRowComponent from "./RoomRowComponent.tsx";
import { useTranslation } from "react-i18next";
import { useEffect, useState } from "react";
import { isInstanceOf } from "../utils";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { PaginationRequestParamsSchema } from "../validation/schemas.ts";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import FormComponent from "./FormComponent.tsx";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import { useLocations } from "../hooks/useLocations.ts";
import {
  PaginationRoomResponse,
  RoomsWithNumberOfElements,
} from "../types/Room.ts";

export function ManageRoomsComponent({ locationId }: { locationId: string }) {
  const { t } = useTranslation();
  const { getRoomsByLocationIdWithPagination } = useLocations();
  const [rooms, setRooms] = useState<RoomsWithNumberOfElements | null>();
  const [open, setOpen] = useState(false);

  const { handleSubmit, control, setValue, getValues } =
    useForm<PaginationRequestParams>({
      defaultValues: {
        page: 0,
        size: 5,
        direction: "asc",
        key: "id",
      },
      resolver: yupResolver(PaginationRequestParamsSchema),
    });

  async function getRooms() {
    const response = await getRoomsByLocationIdWithPagination(
      locationId,
      getValues(),
    );
    if (isInstanceOf<PaginationRoomResponse>(response, "pageable")) {
      setRooms({
        numberOfElements: response.numberOfElements,
        rooms: response.content,
      });
    }
  }

  const onSubmit: SubmitHandler<PaginationRequestParams> = async () => {
    await getRooms();
    setOpen(false);
  };

  useEffect(() => {
    getRooms();
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
      <Typography variant="h4">{t("manageRooms")}</Typography>
      <Button
        onClick={getRooms}
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
                    <MenuItem value="name">{t("roomName")}</MenuItem>
                    <MenuItem value="maxCapacity">{t("maxCapacity")}</MenuItem>
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
      <TableContainer>
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
              {t("roomName")}
            </TableCell>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="right"
            >
              {t("maxCapacity")}
            </TableCell>
          </TableHead>
          <TableBody>
            {rooms?.rooms.map((room) => {
              return (
                <RoomRowComponent key={room.id} room={room}></RoomRowComponent>
              );
            })}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={rooms?.numberOfElements ?? 0}
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