import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";

import { Event } from "../types/Event";
import { Grid } from "@mui/material";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

interface Props {
  event: Event | undefined;
}

export default function EventBigCardComponent(props: Props) {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <Grid item xs={12} md={6}>
      <Card>
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {props.event?.name}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {props.event?.description}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {t("startTime")}: {props.event?.startDate.substring(0, 10)}
          </Typography>

          <Typography variant="body2" color="text.secondary">
            {t("endTime")}: {props.event?.endDate.substring(0, 10)}
          </Typography>
        </CardContent>
        <CardActions>
          <Button
            onClick={() => navigate(`/events/${props.event?.id}`)}
            size="small"
          >
            {t("learnMore")}
          </Button>
        </CardActions>
      </Card>
    </Grid>
  );
}
