CREATE TABLE IF NOT EXISTS terminal_request (
    id uuid PRIMARY KEY,
    status VARCHAR(255) NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    terminal_type VARCHAR(255) NOT NULL,
    terminal_id uuid,
    tracking_id uuid,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(50) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Add comments
COMMENT ON TABLE terminal_request IS 'Stores terminal request information for terminal requests';

COMMENT ON COLUMN terminal_request.id IS 'Unique identifier';
COMMENT ON COLUMN terminal_request.status IS 'Terminal request status';
COMMENT ON COLUMN terminal_request.customer_id IS 'Customer identifier';
COMMENT ON COLUMN terminal_request.terminal_type IS 'Terminal type (e.g., POS_WIFI)';
COMMENT ON COLUMN terminal_request.terminal_id IS 'Terminal identifier assigned';
COMMENT ON COLUMN terminal_request.tracking_id IS 'Tracking code for delivery';

COMMENT ON COLUMN terminal_request.street IS 'Address street';
COMMENT ON COLUMN terminal_request.number IS 'Address number';
COMMENT ON COLUMN terminal_request.city IS 'Address city';
COMMENT ON COLUMN terminal_request.state IS 'Address state (UF)';
COMMENT ON COLUMN terminal_request.zip_code IS 'Address ZIP code';

COMMENT ON COLUMN terminal_request.created_at IS 'Creation timestamp';
COMMENT ON COLUMN terminal_request.updated_at IS 'Last update timestamp';