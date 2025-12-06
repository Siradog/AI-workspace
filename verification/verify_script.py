from playwright.sync_api import sync_playwright

def verify_app():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

        # Navigate to the app (default Vite port is 5173)
        page.goto("http://localhost:5173")

        # Wait for app to load
        page.wait_for_selector("text=AI System Prompt Composer")

        # 1. Modify the first block
        # Find the first input (Title) and type "My Custom Role"
        # We need to target the specific input. The BlockCard has an input for title.
        # Let's target by placeholder "Block Title (e.g., Role)"
        title_inputs = page.get_by_placeholder("Block Title (e.g., Role)")
        first_title_input = title_inputs.first
        first_title_input.fill("My Custom Role")

        # Modify the content of the first block
        content_areas = page.get_by_placeholder("Enter block content...")
        first_content_area = content_areas.first
        first_content_area.fill("This is my custom role description.")

        # 2. Add a new block
        page.get_by_role("button", name="Add Block").click()

        # 3. Verify Preview Text
        # The preview pane should update. We can check for the text "My Custom Role" in the preview.
        # The preview renders markdown. "## My Custom Role" should be visible.
        # We can just take a screenshot to verify visually.

        # 4. Drag and Drop (Optional / Advanced)
        # It's hard to verify drag and drop via screenshot without a video, but we can try to drag if needed.
        # For now, let's verify text input and preview updates.

        page.wait_for_timeout(1000) # Wait for UI to settle

        # Take screenshot
        page.screenshot(path="verification/app_verification.png", full_page=True)

        browser.close()

if __name__ == "__main__":
    verify_app()
