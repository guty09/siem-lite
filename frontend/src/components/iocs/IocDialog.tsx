import { useEffect, useState } from "react";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    MenuItem,
    Stack,
    TextField,
} from "@mui/material";

import type {
    IndicatorOfCompromise,
    IndicatorOfCompromiseRequest,
} from "../../types/ioc";

const IOC_TYPES = ["IP_ADDRESS", "DOMAIN", "URL", "FILE_HASH"];

interface Props {
    open: boolean;
    ioc: IndicatorOfCompromise | null;
    onClose: () => void;
    onSave: (request: IndicatorOfCompromiseRequest) => Promise<void>;
}

const emptyForm: IndicatorOfCompromiseRequest = {
    value: "",
    type: "IP_ADDRESS",
    threatSource: "",
    confidence: 50,
    malwareFamily: "",
    campaign: "",
    description: "",
};

export default function IocDialog({ open, ioc, onClose, onSave }: Props) {
    const [form, setForm] = useState<IndicatorOfCompromiseRequest>(emptyForm);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        if (ioc) {
            setForm({
                value: ioc.value ?? "",
                type: ioc.type ?? "IP_ADDRESS",
                threatSource: ioc.threatSource ?? "",
                confidence: ioc.confidence ?? 50,
                malwareFamily: ioc.malwareFamily ?? "",
                campaign: ioc.campaign ?? "",
                description: ioc.description ?? "",
            });
        } else {
            setForm(emptyForm);
        }
    }, [ioc, open]);

    function updateField(
        field: keyof IndicatorOfCompromiseRequest,
        value: string | number
    ) {
        setForm((current) => ({
            ...current,
            [field]: value,
        }));
    }

    async function handleSave() {
        setSaving(true);

        try {
            await onSave(form);
            onClose();
        } finally {
            setSaving(false);
        }
    }

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>{ioc ? "Edit IOC" : "Add IOC"}</DialogTitle>

            <DialogContent>
                <Stack spacing={2} sx={{ mt: 1 }}>
                    <TextField
                        label="IOC Value"
                        value={form.value}
                        onChange={(event) => updateField("value", event.target.value)}
                        fullWidth
                        required
                    />

                    <TextField
                        label="IOC Type"
                        value={form.type}
                        onChange={(event) => updateField("type", event.target.value)}
                        select
                        fullWidth
                        required
                    >
                        {IOC_TYPES.map((type) => (
                            <MenuItem key={type} value={type}>
                                {type}
                            </MenuItem>
                        ))}
                    </TextField>

                    <TextField
                        label="Threat Source"
                        value={form.threatSource}
                        onChange={(event) =>
                            updateField("threatSource", event.target.value)
                        }
                        fullWidth
                    />

                    <TextField
                        label="Confidence"
                        type="number"
                        value={form.confidence}
                        onChange={(event) =>
                            updateField("confidence", Number(event.target.value))
                        }
                        fullWidth
                        slotProps={{
                            htmlInput: {
                                min: 0,
                                max: 100,
                            },
                        }}
                    />

                    <TextField
                        label="Malware Family"
                        value={form.malwareFamily}
                        onChange={(event) =>
                            updateField("malwareFamily", event.target.value)
                        }
                        fullWidth
                    />

                    <TextField
                        label="Campaign"
                        value={form.campaign}
                        onChange={(event) => updateField("campaign", event.target.value)}
                        fullWidth
                    />

                    <TextField
                        label="Description"
                        value={form.description}
                        onChange={(event) =>
                            updateField("description", event.target.value)
                        }
                        fullWidth
                        multiline
                        minRows={3}
                    />
                </Stack>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>

                <Button
                    variant="contained"
                    onClick={handleSave}
                    disabled={saving || form.value.trim() === ""}
                >
                    {saving ? "Saving..." : "Save"}
                </Button>
            </DialogActions>
        </Dialog>
    );
}