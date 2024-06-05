import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

import { Event } from '../types/Event';
import CenteredContainerComponent from './CenterdContainerComponent';

interface Props {
  event: Event,
}

export default function EventCarouselComponent(props: Props) {
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
          <Button size="small">Learn More</Button>
        </CardActions>
      </Card>
    </CenteredContainerComponent>
  );
}