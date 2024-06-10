import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";

import { Event } from "../types/Event";
import CenteredContainerComponent from "./CenterdContainerComponent";
import { useNavigate } from "react-router-dom";

interface Props {
  event: Event;
}

export default function EventCarouselComponent(props: Props) {
  const navigate = useNavigate();

  return (
    <CenteredContainerComponent>
      <Card sx={{ maxWidth: 345 }}>
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {props.event.name}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {props.event.description}
          </Typography>
        </CardContent>
        <CardActions>
          <Button
            size="small"
            onClick={() => navigate(`/events/${props.event?.id}`)}
          >
            Learn More
          </Button>
        </CardActions>
      </Card>
    </CenteredContainerComponent>
  );
}
