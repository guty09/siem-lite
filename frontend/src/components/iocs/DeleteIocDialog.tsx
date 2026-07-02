import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Typography,
} from "@mui/material";

import type { IndicatorOfCompromise } from "../../types/ioc";

interface Props {
    open: boolean;
    ioc: IndicatorOfCompromise | null;
    onClose: () => void;
    onConfirm: () => Promise<void>;
}

export default function DeleteIocDialog({
                                            open,
                                            ioc,
                                            onClose,
                                            onConfirm,
                                        }: Props) {
    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Delete IOC</DialogTitle>

            <DialogContent>
                <Typography>
                    Are you sure you want to delete this IOC?
                </Typography>

                <Typography sx={{ mt: 2, fontFamily: "monospace", fontWeight: 700 }}>
                    {ioc?.value}
                </Typography>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>

                <Button color="error" variant="contained" onClick={onConfirm}>
                    Delete
                </Button>
            </DialogActions>
        </Dialog>
    );
}